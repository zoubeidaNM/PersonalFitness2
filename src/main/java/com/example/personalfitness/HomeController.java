package com.example.personalfitness;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RequestRepository requestRepository;


    @Autowired
    StringRepository areas;

    @Autowired
    StringRepository Specialties;
    

    @Autowired
    CloudinaryConfig cloudc;

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

        model.addAttribute("user", user);
        return "trainer";
    }


    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping(value="/register", method = RequestMethod.GET)
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new FitnessUser());
        return "registeruser";
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("user") FitnessUser user,
                                          BindingResult result, @RequestParam String role, Model model) {
        if (result.hasErrors()) {
            return "registeruser";
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

                return "registeruser";
            }
            model.addAttribute("user", user);

            System.out.println(user.getFirstName());
            System.out.println(user.getArea());
            System.out.println(user.getNeedOrSpecialty());

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

        model.addAttribute("names", names);

        model.addAttribute("request", request);
        return "requestform";
    }

    @PostMapping("/user/request")
    public String processUserRequest(Principal principal, @Valid @ModelAttribute("request") Request request,
                                     BindingResult result, Model model){
        FitnessUser user = userRepository.findByUsername(principal.getName());
        if (result.hasErrors()) {
            System.out.println(result.toString());
            return "requestform";
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

         if (acceptOrDecline.equalsIgnoreCase("accept")) {
            // user clicked "accept"
            request.setAnswered(true);
            request.setShowTrainer(true);
            request.setReceiverAnswer("Accepted");
            request.setStatus("Accepted");

        } else if (acceptOrDecline.equalsIgnoreCase("decline")) {
            // user clicked "decline"
            request.setAnswered(true);
            request.setStatus("Conflict");
            request.setReceiverAnswer("Declined");
            request.setShowTrainer(false);
            trainerUser.discardDeniedRequest(request);
        }

        requestRepository.save(request);
        FitnessUser user = userRepository.findByUsername(request.getSenderName());
        user.setUserRequestFlag(true);
        trainerUser.setTrainerRequestFlag(false);
        model.addAttribute("user", trainerUser);

        return "redirect:/trainer";
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

            System.out.println(transformedImage);
            System.out.println("Uploaded:" + uploadURL);
            System.out.println("Name:" + uploadedName);

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

            System.out.println(transformedImage);
            System.out.println("Uploaded:" + uploadURL);
            System.out.println("Name:" + uploadedName);

            user.setHeadshot(transformedImage);
            userRepository.save(user);
            model.addAttribute("user", user);

        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/trainer/choosepic";
        }

        return "trainer";
    }


}
