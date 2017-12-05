package com.example.personalfitness;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

     @Autowired
    RequestRepository requestRepository;


    @Autowired
    AreaRepository areas;

    @Autowired
    SpecialtyRepository specialties;

    @Autowired
    FitnessLevelRepository fitnessLevels;


    @Autowired
    CloudinaryConfig cloudc;

    @Autowired
    CommentRepository commentRepository;

    @RequestMapping("/")
    public String index() {
        return "homepage";
    }

    @RequestMapping("/welcome")
    public String welcome(Principal principal, Model model) {


        FitnessUser user = userRepository.findByUsername(principal.getName());

        model.addAttribute("user", user);
        return "welcome";
    }


    @RequestMapping("/user")
    public String user(Principal principal, Model model) {


        FitnessUser user = userRepository.findByUsername(principal.getName());

        model.addAttribute("user", user);
        LinkedHashSet<Request> requests =requestRepository.findAllBySenderNameOrderByPosteddateDesc(user.getUsername());

        model.addAttribute("requests", requests);
        return "user";
    }


    @RequestMapping("/trainer")
    public String trainer(Principal principal, Model model) {


        FitnessUser user = userRepository.findByUsername(principal.getName());
        LinkedHashSet<Request> requests = requestRepository.findAllByReceiverNameOrderByPosteddateDesc(user.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("requests", requests);
        return "trainer";
    }


    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistrationPage(Model model) {
        model.addAttribute("fitnessLevels", fitnessLevels.findAll());
        model.addAttribute("areas", areas.findAll());
        model.addAttribute("specialties", specialties.findAll());
        model.addAttribute("user", new FitnessUser());
        return "registeruser";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("user") FitnessUser user,
                                          BindingResult result, @RequestParam String role, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.toString());
            return "redirect:/register";
        } else {

            if (userRepository.findByUsername(user.getUsername()) == null) {

                // add the user to chosen specialtied
                for (Specialty specialty : user.getSpecialties()) {
                    specialty.addUser(user);
                }
                // add the user to chosen fitness level
                for (FitnessLevel fitnessLevel : user.getFitnessLevels()) {
                    fitnessLevel.addUser(user);
                }
                if (role.equalsIgnoreCase("USER")) {
                    userService.saveUser(user);
                } else if (role.equalsIgnoreCase("TRAINER")) {
                    userService.saveTrainer(user);
                }
                model.addAttribute("message", "User Account Successfully Created");
            } else if (userRepository.findByUsername(user.getUsername()) == null) {
                model.addAttribute("error", true);
                model.addAttribute("error_message", "Username already exists. Try again!");

                return "redirect:/register";
            }
            model.addAttribute("user", user);

            System.out.println(user.getFirstName());
            System.out.printf("FitnessLevels size" + user.getFitnessLevels().size());

            return "login";
        }
    }


    @RequestMapping("/user/request")
    public String showRequest(Principal principal, Model model) {

        FitnessUser user = userRepository.findByUsername(principal.getName());
        user.setUserRequestFlag(false);
        userRepository.save(user);

        Request request = new Request();

        //set default dates
        request.setDay(1);
        request.setYear(2017);
        request.setHours(14);
        request.setMinutes(30);

        String names[] = get_trainerNames();
        model.addAttribute("names", names);

        //get the areas
        model.addAttribute("areas", areas.findAll());

        model.addAttribute("request", request);
        return "requestform";
    }

    @PostMapping("/user/request")
    public String processUserRequest(Principal principal, @Valid @ModelAttribute("request") Request request,
                                     BindingResult result, Model model) {
        FitnessUser user = userRepository.findByUsername(principal.getName());
        if (result.hasErrors()) {
            System.out.println(result.toString());
            String names[] = get_trainerNames();
            model.addAttribute("names", names);

            //get the areas
            model.addAttribute("areas", areas.findAll());
            return "requestform";
        } else {
            Calendar calendar = Calendar.getInstance();
            java.sql.Date ourJavaDateObject = new java.sql.Date(calendar.getTime().getTime());

            // validate date

            //validate trainerName

            //get trainer names:
            ArrayList<FitnessUser> trainers = getUsersList("TRAINER");
            if (!trainers.contains(userRepository.findByUsername(request.getReceiverName()))) {
                String names[] = get_trainerNames();
                //get the areas
                model.addAttribute("areas", areas.findAll());
                model.addAttribute("names", names);
                model.addAttribute("error", true);
                model.addAttribute("error_message", "No trainer with name: " + request.getReceiverName() + ". Try again!");
                return "requestform";
            }

            if(!request.processDate()) {
                String names[] = get_trainerNames();
                //get the areas
                model.addAttribute("areas", areas.findAll());
                model.addAttribute("names", names);
                model.addAttribute("error", true);
                model.addAttribute("error_message", "Choose a valid Date. Try again!");
                return "requestform";
            }

            if (!request.isDateInTheFuture()){
                String names[] = get_trainerNames();
            //get the areas
            model.addAttribute("areas", areas.findAll());
            model.addAttribute("names", names);
            model.addAttribute("error", true);
            model.addAttribute("error_message", "The date chosen must be in the future. Try again!");
                return "requestform";
        }
            request.setPosteddate(ourJavaDateObject);
            request.processTime();
            request.setStatus("Waiting");
            request.setReceiverAnswer("Waiting");
            request.setSenderName(user.getUsername());


            FitnessUser trainer = userRepository.findByUsername(request.getReceiverName());
            trainer.setTrainerRequestFlag(true);

            requestRepository.save(request);
            user.setRequests(requestRepository.findAllBySenderNameOrderByPosteddateDesc(user.getUsername()));
            user.addRequest(request);
            trainer.setRequests(requestRepository.findAllByReceiverNameOrderByPosteddateDesc(trainer.getUsername()));

            trainer.addRequest(request);

            userRepository.save(user);
            userRepository.save(trainer);

            model.addAttribute("user", user);
            LinkedHashSet<Request> requests =requestRepository.findAllBySenderNameOrderByPosteddateDesc(user.getUsername());

            model.addAttribute("requests", requests);

            return "user";
        }
    }

    @RequestMapping("/user/request/{id}")
    public String sendRequest(@PathVariable("id") long id, RedirectAttributes redirectAttributes, Model model) {


        FitnessUser trainer = userRepository.findOne(id);

        Request request = new Request();

        request.setReceiverName(trainer.getUsername());

        //set default dates
        request.setDay(1);
        request.setYear(2017);
        request.setHours(14);
        request.setMinutes(30);

        String names[] = get_trainerNames();
        model.addAttribute("names", names);

        //get the areas
        model.addAttribute("areas", areas.findAll());

        model.addAttribute("request", request);
        return "requestform";
    }


    @RequestMapping("/trainer/requests/{id}")
    public String showTrainerRequest(@PathVariable("id") long id, Model model) {
        model.addAttribute("request", requestRepository.findOne(id));
        return "answerrequest";
    }

    @PostMapping("/trainer/requests/{id}")
    public String processTrainerRequest(Principal principal, @PathVariable("id") long id,
                                        @RequestParam(value = "acceptParam") String acceptOrDecline, Model model) {

        FitnessUser trainerUser = userRepository.findByUsername(principal.getName());
        Request request = requestRepository.findOne(id);
        FitnessUser user = userRepository.findByUsername(request.getSenderName());

        if (acceptOrDecline.equalsIgnoreCase("accept")) {
            // user clicked "accept"
            request.setAnswered(true);
            request.setShowTrainer(true);
            request.setReceiverAnswer("Accepted");
            request.setStatus("Accepted");
            trainerUser.addRatableUserName(user.getUsername());
            user.addRatableUserName(trainerUser.getUsername());
            user.setUserRequestFlag(true);

        } else if (acceptOrDecline.equalsIgnoreCase("decline")) {
            // user clicked "decline"
            request.setAnswered(true);
            request.setStatus("Conflict");
            request.setReceiverAnswer("Declined");
            request.setShowTrainer(false);
            trainerUser.discardDeniedRequest(request);
            user.setUserRequestFlag(true);
        }

        requestRepository.save(request);
        trainerUser.setTrainerRequestFlag(false);
        userRepository.save(user);
        model.addAttribute("user", trainerUser);

        return "redirect:/trainer";
    }

    @RequestMapping("/user/comment")
    public String showComment(Principal principal, Model model) {
        FitnessUser user = userRepository.findByUsername(principal.getName());

        user.setUserRequestFlag(false);
        userRepository.save(user);

        Comment comment = new Comment();
        model.addAttribute("user", user);
        model.addAttribute("comment", comment);
        return "commentform";
    }

    /* ProcessFormText*/
    @PostMapping("/user/comment")
    public String processUserComment(Principal principal, @RequestParam(required = false, name = "isAnonymous") String isAnonymous,
                                     @RequestParam(required = false, name = "rating") Integer rating, @Valid @ModelAttribute("comment") Comment comment,
                                     BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.toString());
            return "commentform";
        }

        FitnessUser user = userRepository.findByUsername(principal.getName());


        if (comment.getTrainerName() == null) {

            model.addAttribute("error", true);
            model.addAttribute("error_message", "You have to select a trainer that has trained you.");
            model.addAttribute("user", user);
            System.out.println("You have to select a trainer that has trained you.");
            return "commentform";
        }

        if (comment.getRating() == 0) {
            rating = 0;

            model.addAttribute("error", true);
            model.addAttribute("error_message", "Please enter a rating.");
            model.addAttribute("user", user);
            System.out.println("Please enter a rating.");
            return "commentform";
        }


        System.out.println("Rating: " + rating);
        Calendar calendar = Calendar.getInstance();
        java.sql.Date ourJavaDateObject = new java.sql.Date(calendar.getTime().getTime());
//        String username = principal.getName();
//        FitnessUser user_current = userRepository.findByUsername(username);
        comment.addUser(user);
        System.out.println("isAnonymous: "+ isAnonymous);
        if (isAnonymous.equals("isAnonymous")) {
            comment.setSentby("Anonymous");
        } else {
            comment.setSentby(user.getUsername());
        }
        comment.setPosteddate(ourJavaDateObject);
        comment.setRating(rating);
        FitnessUser trainer = userRepository.findByUsername(comment.getTrainerName());
        comment.setUserName(user.getUsername());
        commentRepository.save(comment);
        trainer.addComment(comment);
        trainer.computeAverageRating();
        System.out.println("Trainer: " + trainer.getUsername());
        System.out.println("Comments size: " + trainer.getComments().size());
        userRepository.save(trainer);
        model.addAttribute("user", user);
        LinkedHashSet<Request> requests =requestRepository.findAllBySenderNameOrderByPosteddateDesc(user.getUsername());

        model.addAttribute("requests", requests);
        return "user";

    }


    @RequestMapping("/trainer/comment")
    public String showTrainerComment(Principal principal, Model model) {
        FitnessUser user = userRepository.findByUsername(principal.getName());

        Comment comment = new Comment();
        model.addAttribute("user", user);
        model.addAttribute("comment", comment);
        return "commentform";
    }

    /* ProcessFormText*/
    @PostMapping("/trainer/comment")
    public String processTrainerComment(Principal principal,@RequestParam(required = false, name = "isAnonymous") String isAnonymous, @RequestParam(required = false, name = "rating") Integer rating, @Valid @ModelAttribute("comment") Comment comment,
                                        BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.toString());
            return "commentform";
        }

        FitnessUser user = userRepository.findByUsername(principal.getName());


        if (comment.getUserName() == null) {

            model.addAttribute("error", true);
            model.addAttribute("error_message", "You have to select a user that has trained with you");
            model.addAttribute("user", user);
            System.out.println("You have to select a user that has trained with you.");
            return "commentform";
        }

        if (comment.getRating() == 0) {
            rating = 0;

            model.addAttribute("error", true);
            model.addAttribute("error_message", "Please enter a rating.");
            model.addAttribute("user", user);
            System.out.println("Please enter a rating.");
            return "commentform";
        }


        System.out.println("Rating: " + rating);
        Calendar calendar = Calendar.getInstance();
        java.sql.Date ourJavaDateObject = new java.sql.Date(calendar.getTime().getTime());
        comment.addUser(user);
        if(isAnonymous==null){
            isAnonymous="";
        }
        System.out.println("isAnonymous: "+ isAnonymous);
        if (isAnonymous.equals("isAnonymous")) {
            comment.setSentby("Anonymous");
        } else {
            comment.setSentby(user.getUsername());
        }
        comment.setPosteddate(ourJavaDateObject);
        comment.setRating(rating);
        FitnessUser userAbout = userRepository.findByUsername(comment.getUserName());
        comment.setTrainerName(user.getUsername());
        commentRepository.save(comment);
        userAbout.addComment(comment);
        userAbout.computeAverageRating();
        System.out.println("userAbout: " + userAbout.getUsername());
        System.out.println("Comments size: " + userAbout.getComments().size());

        userRepository.save(userAbout);
        model.addAttribute("user", user);
        LinkedHashSet<Request> requests =requestRepository.findAllByReceiverNameOrderByPosteddateDesc(user.getUsername());

        model.addAttribute("requests", requests);

        return "trainer";

    }

    @RequestMapping("/user/choosepic")
    public String choosePicture(Principal principal, Model model) {
        FitnessUser user = userRepository.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "choosepicture";
    }

    @PostMapping("/user/choosepic")
    public String processPicture(Principal principal, MultipartHttpServletRequest request, Model model) {
        FitnessUser user = userRepository.findByUsername(principal.getName());
        MultipartFile f = request.getFile("imagefile");
        if (f.isEmpty()) {
            return "redirect:/user/choosepic";
        }
        try {
            Map uploadResult = cloudc.upload(f.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));

            String uploadURL = (String) uploadResult.get("url");
            String uploadedName = (String) uploadResult.get("public_id");

            String transformedImage = cloudc.createUrl(uploadedName);

            user.setHeadshot(transformedImage);
            userRepository.save(user);
            model.addAttribute("user", user);

        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/user/choosepic";
        }
        LinkedHashSet<Request> requests =requestRepository.findAllBySenderNameOrderByPosteddateDesc(user.getUsername());

        model.addAttribute("requests", requests);
        return "user";
    }

    @RequestMapping("/trainer/choosepic")
    public String chooseTrainerPicture(Principal principal, Model model) {
        FitnessUser user = userRepository.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "choosepicture";
    }

    @PostMapping("/trainer/choosepic")
    public String processTrainerPicture(Principal principal, MultipartHttpServletRequest request, Model model) {
        FitnessUser user = userRepository.findByUsername(principal.getName());
        MultipartFile f = request.getFile("imagefile");
        if (f.isEmpty()) {
            return "redirect:/trainer/choosepic";
        }
        try {
            Map uploadResult = cloudc.upload(f.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));

            String uploadURL = (String) uploadResult.get("url");
            String uploadedName = (String) uploadResult.get("public_id");

            String transformedImage = cloudc.createUrl(uploadedName);

            user.setHeadshot(transformedImage);
            userRepository.save(user);
            model.addAttribute("user", user);

        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/trainer/choosepic";
        }

        return "redirect:/trainer";
    }


    @RequestMapping("/user/search")
    public String trainerSearch(Principal principal, Model model) {

        ArrayList<FitnessUser> searchedUsers = getUsersList("TRAINER");
        FitnessUser user = userRepository.findByUsername(principal.getName());
        user.setUserRequestFlag(false);
        userRepository.save(user);
       model.addAttribute("field", "all");
        model.addAttribute("searchedUsers", searchedUsers);
        model.addAttribute("user", user);

        return "searchresult";
    }

    @PostMapping("/user/search")
    public String searchRepository(Principal principal, @RequestParam String searchField,
                                   @RequestParam String searchStr, Model model) {
        ArrayList<FitnessUser> trainers = new ArrayList<FitnessUser>();
        FitnessUser user = userRepository.findByUsername(principal.getName());
        System.out.println("field " + searchField + " name" + searchStr + "*");

        if (searchField.equalsIgnoreCase("all")) {
            return "redirect:/user/search";
        } else if (searchField.equalsIgnoreCase("name")) {
            Iterable<FitnessUser> users = userRepository.findAllByUsernameContainingIgnoreCase(searchStr);
            trainers = getUsersList("TRAINER", users);
            model.addAttribute("field", "name");
            model.addAttribute("searchstr", searchStr);
            model.addAttribute("searchedUsers", trainers);
            model.addAttribute("user", user);
        } else if (searchField.equalsIgnoreCase("gender")) {
            Iterable<FitnessUser> users = userRepository.findAllByGenderIgnoreCase(searchStr);
            trainers = getUsersList("TRAINER", users);
            model.addAttribute("field", "gender");
            model.addAttribute("searchstr", searchStr);
            model.addAttribute("searchedUsers", trainers);
            model.addAttribute("user", user);
        } else if (searchField.equalsIgnoreCase("rating")) {
            try {
                System.out.println(" Rating = " + searchStr + " *");
                int rating = Integer.parseInt(searchStr.trim());
                Iterable<FitnessUser> users = userRepository.findAllByAverageRating(rating);
                //System.out.println("RatingSearchSize "+ users.size());
                trainers = getUsersList("TRAINER", users);
                model.addAttribute("field", "rating");
                model.addAttribute("searchstr", searchStr);
                model.addAttribute("searchedUsers", trainers);
                model.addAttribute("user", user);
            } catch (Exception e) {
                System.out.println(e.toString());
                model.addAttribute("user", user);
                System.out.println("Rating have to be an int");
            }
        } else if (searchField.equalsIgnoreCase("specialty")) {
            Iterable<Specialty> specialties1 = specialties.findAllByNameContainingIgnoreCase(searchStr);
            Set<FitnessUser> users2 = new HashSet<FitnessUser>();
            //find the users who are trainers
            for (Specialty specialty : specialties1) {
                for (FitnessUser user1 : specialty.getUsers()) {
                    System.out.println("Specialty size " + specialty.getUsers().size());
                    users2.add(user1);
                }
            }

            for (Specialty specialty : specialties1) {
                for (FitnessUser user1 : specialty.getUsers()) {
                    System.out.println("Specialty size " + specialty.getUsers().size());
                }
            }
            System.out.println("Specialty size " + users2.size());
            trainers = getUsersList("TRAINER", users2);
            System.out.println("Specialty size " + trainers.size());
            model.addAttribute("field", "specialty");
            model.addAttribute("searchstr", searchStr);
            model.addAttribute("searchedUsers", trainers);
            model.addAttribute("user", user);
        } else if (searchField.equalsIgnoreCase("fitnessLevel")) {

            Iterable<FitnessLevel> fitnesslevels1 = fitnessLevels.findAllByNameContainingIgnoreCaseOrNameContainingIgnoreCase(searchStr, "All");
            Set<FitnessUser> users2 = new HashSet<FitnessUser>();
            //find the users who are trainers
            for (FitnessLevel fitnessLevel : fitnesslevels1) {
                for (FitnessUser user1 : fitnessLevel.getFitnessLevelUsers()) {
                    System.out.println(user.getUsername());
                    System.out.println("FitnessLevel size " + fitnessLevel.getFitnessLevelUsers().size());
                    users2.add(user1);
                }
            }

            for (FitnessLevel fitnessLevel : fitnesslevels1) {
                for (FitnessUser user1 : fitnessLevel.getFitnessLevelUsers()) {
                    System.out.println("FitnessLevel size " + fitnessLevel.getFitnessLevelUsers().size());
                }
            }
            System.out.println("FitnessLevel size " + users2.size());
            trainers = getUsersList("TRAINER", users2);
            System.out.println("FitnessLevel size " + trainers.size());
            model.addAttribute("field", "fitnessLevel");
            model.addAttribute("searchstr", searchStr);
            model.addAttribute("searchedUsers", trainers);
            model.addAttribute("user", user);
        }
        return "searchresult";

    }

    @RequestMapping("/trainer/search")
    public String userSearch(Principal principal, Model model) {

        ArrayList<FitnessUser> searchedUsers = getUsersList("USER");
        FitnessUser user = userRepository.findByUsername(principal.getName());

        model.addAttribute("searchedUsers", searchedUsers);
        model.addAttribute("user", user);

        return "searchresult";
    }

    @RequestMapping("/admin")
    public String admin(Principal principal, Model model) {

        FitnessUser user = userRepository.findByUsername(principal.getName());

        ArrayList<FitnessUser> users = getCompleteUsersList("USER");
        ArrayList<FitnessUser> trainers = getCompleteUsersList("TRAINER");



        model.addAttribute("user", user);
        model.addAttribute("manageUsers", false);
        model.addAttribute("manageTrainers", false);
        model.addAttribute("manageAreas", false);
        model.addAttribute("manageSpecialties", false);
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("trainers", userRepository.findAll());
        model.addAttribute("areas", areas.findAll());
        model.addAttribute("specialties", specialties.findAll());
        return "admin";
    }

    @RequestMapping("/admin/suspended")
    public String namageSuspended(Principal principal, Model model) {
        FitnessUser user = userRepository.findByUsername(principal.getName());

        ArrayList<FitnessUser> users = getCompleteUsersList("USER");
        ArrayList<FitnessUser> trainers = getCompleteUsersList("TRAINER");

        model.addAttribute("user", user);
        model.addAttribute("manageUsers", false);
        model.addAttribute("manageTrainers", false);
        model.addAttribute("manageAreas", false);
        model.addAttribute("manageSpecialties", false);
        model.addAttribute("users", users);
        model.addAttribute("trainers", trainers);
        model.addAttribute("areas", areas.findAll());
        model.addAttribute("specialties", specialties.findAll());
        return "redirect:/admin";

    }

    @RequestMapping("/admin/users")
    public String namageUsers(Principal principal, Model model) {
        FitnessUser user = userRepository.findByUsername(principal.getName());

        ArrayList<FitnessUser> users = getCompleteUsersList("USER");
        ArrayList<FitnessUser> trainers = getCompleteUsersList("TRAINER");

        model.addAttribute("user", user);
        model.addAttribute("manageUsers", true);
        model.addAttribute("manageTrainers", false);
        model.addAttribute("manageAreas", false);
        model.addAttribute("manageSpecialties", false);
        model.addAttribute("users", users);
        model.addAttribute("trainers", trainers);
        model.addAttribute("areas", areas.findAll());
        model.addAttribute("specialties", specialties.findAll());
        return "admin";
    }

    @RequestMapping("/admin/trainers")
    public String namageTrainers(Principal principal, Model model) {
        FitnessUser user = userRepository.findByUsername(principal.getName());

        ArrayList<FitnessUser> users = getCompleteUsersList("USER");
        ArrayList<FitnessUser> trainers = getCompleteUsersList("TRAINER");

        model.addAttribute("user", user);
        model.addAttribute("manageUsers", false);
        model.addAttribute("manageTrainers", true);
        model.addAttribute("manageAreas", false);
        model.addAttribute("manageSpecialties", false);
        model.addAttribute("users", users);
        model.addAttribute("trainers", trainers);
        model.addAttribute("areas", areas.findAll());
        model.addAttribute("specialties", specialties.findAll());
        return "admin";
    }

    @RequestMapping("/admin/areas")
    public String namageAreas(Principal principal, Model model) {
        FitnessUser user = userRepository.findByUsername(principal.getName());

        ArrayList<FitnessUser> users = getCompleteUsersList("USER");
        ArrayList<FitnessUser> trainers = getCompleteUsersList("TRAINER");

        model.addAttribute("user", user);
        model.addAttribute("manageUsers", false);
        model.addAttribute("manageTrainers", false);
        model.addAttribute("manageAreas", true);
        model.addAttribute("manageSpecialties", false);
        model.addAttribute("users", users);
        model.addAttribute("trainers", trainers);
        model.addAttribute("areas", areas.findAll());
        model.addAttribute("specialties", specialties.findAll());
        return "admin";

    }

    @RequestMapping("/admin/specialties")
    public String namageSpecialties(Principal principal, Model model) {
        FitnessUser user = userRepository.findByUsername(principal.getName());

        ArrayList<FitnessUser> users = getCompleteUsersList("USER");
        ArrayList<FitnessUser> trainers = getCompleteUsersList("TRAINER");

        model.addAttribute("user", user);
        model.addAttribute("manageUsers", false);
        model.addAttribute("manageTrainers", false);
        model.addAttribute("manageAreas", false);
        model.addAttribute("manageSpecialties", true);
        model.addAttribute("users", users);
        model.addAttribute("trainers", trainers);
        model.addAttribute("areas", areas.findAll());
        model.addAttribute("specialties", specialties.findAll());
        return "admin";

    }

    @RequestMapping("/admin/suspenduser/{id}")
    public String suspendUser(@PathVariable("id") long id, Model model) {
        FitnessUser user = userRepository.findOne(id);
        user.setSuspended(true);
        user.removeRole(roleRepository.findByRole("USER"));
        userRepository.save(user);
        user.addRole(roleRepository.findByRole("SUSPENDED"));
        user.setOldRole("USER");
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @RequestMapping("/admin/unsuspenduser/{id}")
    public String unsuspendUser(@PathVariable("id") long id, Model model) {
        FitnessUser user = userRepository.findOne(id);
        user.setSuspended(false);
        user.removeRole(roleRepository.findByRole("SUSPENDED"));
        userRepository.save(user);
        user.addRole(roleRepository.findByRole("USER"));
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @RequestMapping("/admin/suspendtrainer/{id}")
    public String suspendTrainer(@PathVariable("id") long id, Model model) {
        FitnessUser user = userRepository.findOne(id);
        user.setSuspended(true);
        user.removeRole(roleRepository.findByRole("TRAINER"));
        userRepository.save(user);
        user.addRole(roleRepository.findByRole("SUSPENDED"));
        user.setOldRole("TRAINER");
        userRepository.save(user);
        return "redirect:/admin/trainers";
    }

    @RequestMapping("/admin/unsuspendtrainer/{id}")
    public String unsuspendTrainer(@PathVariable("id") long id, Model model) {
        FitnessUser user = userRepository.findOne(id);
        user.setSuspended(false);
        user.removeRole(roleRepository.findByRole("SUSPENDED"));
        userRepository.save(user);
        user.addRole(roleRepository.findByRole("TRAINER"));
        userRepository.save(user);
        return "redirect:/admin/trainers";
    }

    @RequestMapping("/admin/removearea/{id}")
    public String removeArea(@PathVariable("id") long id, Model model) {
        Area area = areas.findOne(id);
        areas.delete(area);
        return "redirect:/admin/areas";
    }

    @RequestMapping("/admin/removespecialty/{id}")
    public String removeSpecialty(@PathVariable("id") long id, Model model) {
        Specialty specialty = specialties.findOne(id);
        specialties.delete(specialty);
        return "redirect:/admin/specialties";
    }


    @PostMapping("/admin/areas")
    public String addArea(@RequestParam String areaName, Model model) {
        if (!areaName.isEmpty()) {
            Area area = new Area(areaName);
            areas.save(area);
        }
        return "redirect:/admin/areas";
    }

    @PostMapping("/admin/specialties")
    public String addSpecialty(@RequestParam String specialtyName, Model model) {
        if (!specialtyName.isEmpty()) {
            Specialty specialty = new Specialty(specialtyName);
            specialties.save(specialty);
        }
        return "redirect:/admin/specialties";
    }

    public ArrayList<FitnessUser> getUsersList(String roleName) {

        ArrayList<FitnessUser> searchedUsers = new ArrayList<FitnessUser>();

        Iterable<FitnessUser> users = userRepository.findAll();

        for (FitnessUser user : users) {
            for (UserRole role : (user.getRoles())) {
                if (role.getRole().equalsIgnoreCase(roleName)) {
                    searchedUsers.add(user);
                    System.out.println(user.getUsername() + "  " + user.isSuspended());
                }
            }
        }
        return searchedUsers;

    }

    public ArrayList<FitnessUser> getCompleteUsersList(String roleName) {

        ArrayList<FitnessUser> searchedUsers = new ArrayList<FitnessUser>();

        Iterable<FitnessUser> users = userRepository.findAll();

        for (FitnessUser user : users) {
            for (UserRole role : (user.getRoles())) {
                if (role.getRole().equalsIgnoreCase(roleName)) {
                    searchedUsers.add(user);
                    System.out.println(user.getUsername() + "  " + user.isSuspended());
                }
            }
            if(user.getOldRole().equalsIgnoreCase(roleName) && !searchedUsers.contains(users)){
                searchedUsers.add(user);
            }
        }
        return searchedUsers;

    }

    public ArrayList<FitnessUser> getUsersList(String roleName, Iterable<FitnessUser> users) {

        ArrayList<FitnessUser> searchedUsers = new ArrayList<FitnessUser>();

        for (FitnessUser user : users) {
            for (UserRole role : (user.getRoles())) {
                if (role.getRole().equalsIgnoreCase(roleName)) {
                    searchedUsers.add(user);
                    System.out.println(user.getUsername() + "  " + user.isSuspended());
                }
            }
        }
        return searchedUsers;

    }

    public String[] get_trainerNames() {
        // get the names of the trainers for the autocomlete
        ArrayList<String> trainerNames = new ArrayList<String>();

        Iterable<FitnessUser> users = userRepository.findAll();
        for (FitnessUser user : users) {
            for (UserRole role : (user.getRoles())) {
                if (role.getRole().equalsIgnoreCase("TRAINER")) {
                    trainerNames.add(user.getUsername());
                    System.out.println(user.getUsername());
                }
            }
        }

        String names[] = new String[trainerNames.size()];
        names = trainerNames.toArray(names);

        return names;

    }


}
