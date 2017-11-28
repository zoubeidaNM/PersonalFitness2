package com.example.personalfitness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @RequestMapping("/")
    public String index(){
        return "homepage";
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
            System.out.println(user.getFirstName());
            model.addAttribute("user", user);

            System.out.println(user.getUsername());
            System.out.println(user.getPassword());
            return "login";
        }
    }




}
