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
    RequestRepository requestRepository;


    @Autowired
    AreaRepository areas;

    @Autowired
    SpecialtyRepository specialties;


    @Autowired
    CloudinaryConfig cloudc;

    @Autowired
    CommentRepository commentRepository;
    @RequestMapping("/")
    public String index(){
        return "homepage";
    }

    @RequestMapping("/welcome")
    public String welcome(Principal principal, Model model){


        FitnessUser user = userRepository.findByUsername(principal.getName());

        model.addAttribute("user", user);
        return "welcome";
    }


    @RequestMapping("/user")
    public String user(Principal principal, Model model){


        FitnessUser user = userRepository.findByUsername(principal.getName());

        model.addAttribute("user", user);
        return "user";
    }


    @RequestMapping("/trainer")
    public String trainer(Principal principal, Model model){


        FitnessUser user = userRepository.findByUsername(principal.getName());
        System.out.println("User: "+user.getUsername() );
        System.out.println("Comments size: "+user.getComments().size());
        model.addAttribute("user", user);
        return "trainer";
    }


    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping(value="/register", method = RequestMethod.GET)
    public String showRegistrationPage(Model model){
        model.addAttribute("areas", areas.findAll());
        model.addAttribute("specialties", specialties.findAll());
        model.addAttribute("user", new FitnessUser());
        return "registeruser";
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("user") FitnessUser user,
                                          BindingResult result, @RequestParam String role, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.toString());
            return "redirect:/register";
        } else {

            if (userRepository.findByUsername(user.getUsername()) == null) {
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
            System.out.printf(""+user.getSpecialties().size());

            return "login";
        }
    }


    @RequestMapping("/user/request")
    public String showRequest(Model model){

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
                                     BindingResult result, Model model){
        FitnessUser user = userRepository.findByUsername(principal.getName());
        if (result.hasErrors()) {
            System.out.println(result.toString());
            return "redirect:/user/request";
        } else {

            request.processDate();
            request.processTime();
            request.setStatus("Waiting");
            request.setReceiverAnswer("Waiting");
            request.setSenderName(user.getUsername());

            FitnessUser trainer = userRepository.findByUsername(request.getReceiverName());
            trainer.setTrainerRequestFlag(true);

            System.out.println(trainer.getFirstName());

            requestRepository.save(request);

            user.addRequest(request);
            trainer.addRequest(request);

            userRepository.save(user);
            userRepository.save(trainer);

            model.addAttribute("user", user);
            return "user";
        }
    }

    @RequestMapping("/user/request/{id}")
    public String sendRequest(@PathVariable("id") long id, RedirectAttributes redirectAttributes, Model model){


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
    public String showTrainerRequest(@PathVariable("id") long id, Model model){
        model.addAttribute("request", requestRepository.findOne(id));
        return "answerrequest";
    }

    @PostMapping("/trainer/requests/{id}")
    public String processTrainerRequest(Principal principal, @PathVariable("id") long id,
                                        @RequestParam(value = "acceptParam") String acceptOrDecline,Model model){

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
        model.addAttribute("user", trainerUser);

        return "redirect:/trainer";
    }

    @RequestMapping("/user/comment")
    public String showComment(Principal principal, Model model){
        FitnessUser user = userRepository.findByUsername(principal.getName());

        Comment comment = new Comment();
        model.addAttribute("user", user);
        model.addAttribute("comment", comment);
        return "commentform";
    }
    /* ProcessFormText*/
    @PostMapping("/user/comment")
    public String processUserComment(Principal principal, @Valid @ModelAttribute("comment") Comment comment, @RequestParam("rating")int rating,
                                     BindingResult result, Model model) {
        FitnessUser user = userRepository.findByUsername(principal.getName());
        if (result.hasErrors()) {
            System.out.println(result.toString());
            return "commentform";
        }

        System.out.println ("Rating: "+ rating );
        Calendar calendar = Calendar.getInstance();
        java.sql.Date ourJavaDateObject = new java.sql.Date(calendar.getTime().getTime());
//        String username = principal.getName();
//        FitnessUser user_current = userRepository.findByUsername(username);
        comment.addUser(user);
        comment.setSentby(user.getUsername());
        comment.setPosteddate(ourJavaDateObject);
        comment.setRating ( rating );
        FitnessUser trainer=userRepository.findByUsername(comment.getTrainerName());
        comment.setUserName(user.getUsername());
        commentRepository.save(comment);
        trainer.addComment(comment);
        trainer.computeAverageRating();
        System.out.println("Trainer: "+trainer.getUsername());
        System.out.println("Comments size: "+trainer.getComments().size());
        userRepository.save(trainer);
        model.addAttribute("user", user);
        return "user";

    }


    @RequestMapping("/trainer/comment")
    public String showTrainerComment(Principal principal,Model model){
        FitnessUser user = userRepository.findByUsername(principal.getName());

        Comment comment = new Comment();
        model.addAttribute("user", user);
        model.addAttribute("comment", comment);
        return "commentform";
    }
    /* ProcessFormText*/
    @PostMapping("/trainer/comment")
    public String processTrainerComment(Principal principal, @Valid @ModelAttribute("comment") Comment comment, @RequestParam("rating")int rating,
                                     BindingResult result, Model model) {
        FitnessUser user = userRepository.findByUsername(principal.getName());
        if (result.hasErrors()) {
            System.out.println(result.toString());
            return "commentform";
        }

        System.out.println ("Rating: "+ rating );
        Calendar calendar = Calendar.getInstance();
        java.sql.Date ourJavaDateObject = new java.sql.Date(calendar.getTime().getTime());
        comment.addUser(user);
        comment.setSentby(user.getUsername());
        comment.setPosteddate(ourJavaDateObject);
        comment.setRating ( rating );
        FitnessUser userAbout=userRepository.findByUsername(comment.getUserName());
        comment.setTrainerName(user.getUsername());
        commentRepository.save(comment);
        userAbout.addComment(comment);
        userAbout.computeAverageRating();
        System.out.println("userAbout: "+userAbout.getUsername());
        System.out.println("Comments size: "+userAbout.getComments().size());

        userRepository.save(userAbout);
        model.addAttribute("user", user);
        return "trainer";

    }

    @RequestMapping("/user/choosepic")
    public String choosePicture(Principal principal, Model model){
        FitnessUser user = userRepository.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "choosepicture";
    }

    @PostMapping("/user/choosepic")
    public String processPicture(Principal principal, MultipartHttpServletRequest request, Model model){
        FitnessUser user = userRepository.findByUsername(principal.getName());
        MultipartFile f = request.getFile("imagefile");
        if (f.isEmpty()) {
            return "redirect:/user/choosepic";
        }
        try{
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

        return "user";
    }

    @RequestMapping("/trainer/choosepic")
    public String chooseTrainerPicture(Principal principal, Model model){
        FitnessUser user = userRepository.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "choosepicture";
    }

    @PostMapping("/trainer/choosepic")
    public String processTrainerPicture(Principal principal, MultipartHttpServletRequest request, Model model){
        FitnessUser user = userRepository.findByUsername(principal.getName());
        MultipartFile f = request.getFile("imagefile");
        if (f.isEmpty()) {
            return "redirect:/trainer/choosepic";
        }
        try{
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

        model.addAttribute("searchedUsers", searchedUsers);
        model.addAttribute("user", user);

        return "searchresult";
    }

    @PostMapping("/user/search")
    public String searchRepository(Principal principal, @RequestParam String searchField,
                                   @RequestParam String searchStr, Model model) {
        ArrayList<FitnessUser> trainers = new ArrayList<FitnessUser>();
        FitnessUser user = userRepository.findByUsername(principal.getName());
        System.out.println("field " + searchField + " name"+ searchStr+"*");

        if (searchField.equalsIgnoreCase("name")) {
            Iterable<FitnessUser> users = userRepository.findAllByUsernameContaining(searchStr);
            trainers = getUsersList("TRAINER", users);
            model.addAttribute("field", "name");
            model.addAttribute("searchstr", searchStr);
            model.addAttribute("searchedUsers", trainers);
            model.addAttribute("user", user);
        } else if (searchField.equalsIgnoreCase("gender")) {
            Iterable<FitnessUser> users = userRepository.findAllByGenderContaining(searchStr);
            trainers = getUsersList("TRAINER", users);
            model.addAttribute("field", "gender");
            model.addAttribute("searchstr", searchStr);
            model.addAttribute("searchedUsers", trainers);
            model.addAttribute("user", user);
    } else if (searchField.equalsIgnoreCase("rating")) {
            try {
                int rating = Integer.parseInt(searchStr);
                Iterable<FitnessUser> users = userRepository.findAllByAverageRatingContaining(rating);
                trainers = getUsersList("TRAINER", users);
                model.addAttribute("field", "rating");
                model.addAttribute("searchstr", searchStr);
                model.addAttribute("searchedUsers", trainers);
                model.addAttribute("user", user);
            }catch (Exception e){
                model.addAttribute("user", user);
                System.out.println("Rating have to be an int");
            }
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
    public String admin(Principal principal, Model model){

        FitnessUser user = userRepository.findByUsername(principal.getName());

        ArrayList<FitnessUser> users = getUsersList("USER");
        ArrayList<FitnessUser> trainers = getUsersList("TRAINER");

        model.addAttribute("user", user);
        model.addAttribute("manageUsers",false);
        model.addAttribute("manageTrainers",false);
        model.addAttribute("manageAreas",false);
        model.addAttribute("manageSpecialties",false);
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("trainers", userRepository.findAll());
        model.addAttribute("areas", areas.findAll());
        model.addAttribute("specialties", specialties.findAll());
        return "admin";
    }
    @RequestMapping("/admin/users")
    public String namageUsers(Principal principal, Model model) {
        FitnessUser user = userRepository.findByUsername(principal.getName());

        ArrayList<FitnessUser> users = getUsersList("USER");
        ArrayList<FitnessUser> trainers = getUsersList("TRAINER");

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

        ArrayList<FitnessUser> users = getUsersList("USER");
        ArrayList<FitnessUser> trainers = getUsersList("TRAINER");

        model.addAttribute("user", user);
        model.addAttribute("manageUsers", false);
        model.addAttribute("manageTrainers", true);
        model.addAttribute("manageAreas", false);
        model.addAttribute("manageSpecialties", false);
        model.addAttribute("users",users);
        model.addAttribute("trainers", trainers);
        model.addAttribute("areas", areas.findAll());
        model.addAttribute("specialties", specialties.findAll());
        return "admin";
    }

        @RequestMapping("/admin/areas")
        public String namageAreas(Principal principal, Model model){
            FitnessUser user = userRepository.findByUsername(principal.getName());

            ArrayList<FitnessUser> users = getUsersList("USER");
            ArrayList<FitnessUser> trainers = getUsersList("TRAINER");

            model.addAttribute("user", user);
            model.addAttribute("manageUsers",false);
            model.addAttribute("manageTrainers",false);
            model.addAttribute("manageAreas",true);
            model.addAttribute("manageSpecialties",false);
            model.addAttribute("users", users);
            model.addAttribute("trainers", trainers);
            model.addAttribute("areas", areas.findAll());
            model.addAttribute("specialties", specialties.findAll());
            return "admin";

        }

        @RequestMapping("/admin/specialties")
        public String namageSpecialties(Principal principal, Model model){
            FitnessUser user = userRepository.findByUsername(principal.getName());

            ArrayList<FitnessUser> users = getUsersList("USER");
            ArrayList<FitnessUser> trainers = getUsersList("TRAINER");

            model.addAttribute("user", user);
            model.addAttribute("manageUsers",false);
            model.addAttribute("manageTrainers",false);
            model.addAttribute("manageAreas",false);
            model.addAttribute("manageSpecialties",true);
            model.addAttribute("users", users);
            model.addAttribute("trainers", trainers);
            model.addAttribute("areas", areas.findAll());
            model.addAttribute("specialties", specialties.findAll());
            return "admin";

        }

    @RequestMapping("/admin/suspenduser/{id}")
    public String suspendUser(@PathVariable("id") long id, Model model){
        FitnessUser user = userRepository.findOne(id);
        user.setSuspended(true);
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @RequestMapping("/admin/unsuspenduser/{id}")
    public String unsuspendUser(@PathVariable("id") long id, Model model){
        FitnessUser user = userRepository.findOne(id);
        user.setSuspended(false);
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @RequestMapping("/admin/suspendtrainer/{id}")
    public String suspendTrainer(@PathVariable("id") long id, Model model){
        FitnessUser user = userRepository.findOne(id);
        user.setSuspended(true);
        userRepository.save(user);
        return "redirect:/admin/trainers";
    }

    @RequestMapping("/admin/unsuspendtrainer/{id}")
    public String unsuspendTrainer(@PathVariable("id") long id, Model model){
        FitnessUser user = userRepository.findOne(id);
        user.setSuspended(false);
        userRepository.save(user);
        return "redirect:/admin/trainers";
    }

    @RequestMapping("/admin/removearea/{id}")
    public String removeArea(@PathVariable("id") long id, Model model){
        Area area = areas.findOne(id);
        areas.delete(area);
        return "redirect:/admin/areas";
    }

    @RequestMapping("/admin/removespecialty/{id}")
    public String removeSpecialty(@PathVariable("id") long id, Model model){
        Specialty specialty = specialties.findOne(id);
        specialties.delete(specialty);
        return "redirect:/admin/specialties";
    }


    @PostMapping("/admin/areas")
    public String addArea(@RequestParam String areaName, Model model){
        if(!areaName.isEmpty())
        {
            Area area = new Area(areaName);
            areas.save(area);
        }
        return "redirect:/admin/areas";
    }

    @PostMapping("/admin/specialties")
    public String addSpecialty(@RequestParam String specialtyName, Model model){
        if(!specialtyName.isEmpty())
        {
            Specialty specialty = new Specialty(specialtyName);
            specialties.save(specialty);
        }
        return "redirect:/admin/specialties";
    }

    public ArrayList<FitnessUser> getUsersList(String roleName){

        ArrayList<FitnessUser> searchedUsers = new ArrayList<FitnessUser>();

        Iterable<FitnessUser> users =  userRepository.findAll();

        for(FitnessUser user:users){
            for(UserRole role: (user.getRoles())){
                if (role.getRole().equalsIgnoreCase(roleName)){
                    searchedUsers.add(user);
                    System.out.println(user.getUsername()+"  " + user.isSuspended());
                }
            }
        }
        return searchedUsers;

    }

    public ArrayList<FitnessUser> getUsersList(String roleName, Iterable<FitnessUser> users){

        ArrayList<FitnessUser> searchedUsers = new ArrayList<FitnessUser>();

        for(FitnessUser user:users){
            for(UserRole role: (user.getRoles())){
                if (role.getRole().equalsIgnoreCase(roleName)){
                    searchedUsers.add(user);
                    System.out.println(user.getUsername()+"  " + user.isSuspended());
                }
            }
        }
        return searchedUsers;

    }

    public String[] get_trainerNames(){
        // get the names of the trainers for the autocomlete
        ArrayList<String> trainerNames = new ArrayList<String>();

        Iterable<FitnessUser> users =  userRepository.findAll();
        for(FitnessUser user:users){
            for(UserRole role: (user.getRoles())){
                if (role.getRole().equalsIgnoreCase("TRAINER")){
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
