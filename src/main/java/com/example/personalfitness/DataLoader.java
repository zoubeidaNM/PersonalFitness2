package com.example.personalfitness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

@Component
class DataLoader implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    @Autowired
    RequestRepository requestRepository;

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("Loading data . . .");

        roleRepository.save(new UserRole("USER"));
        roleRepository.save(new UserRole("TRAINER"));
        roleRepository.save(new UserRole("ADMIN"));

        UserRole userRole = roleRepository.findByRole("USER");
        UserRole trainerRole = roleRepository.findByRole("TRAINER");
        UserRole adminRole = roleRepository.findByRole("ADMIN");


        FitnessUser user = new FitnessUser();
        //"bob@bob.com", true, "bobberson", "Bob", "bob", "bob"
        user.setFirstName("Bob");
        user.setLastName("Bobberson");
        user.setEmail("bob@bob.com");
        user.setContactNumber("301-555-5555");
        user.setEnabled(true);
        user.setUsername("bob");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(userRole));
        user.setFitnessLevel("Beginner");
        user.setArea("Montgomery County");
        user.setGender("Male");
        user.setNeedOrSpecialty("Dance");
        DateTimeFormatter dTF = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter hr24 = DateTimeFormatter.ofPattern("kk:m");

        LocalDate date=LocalDate.parse("11/11/2017",dTF);
        LocalTime time= LocalTime.parse("11:02",hr24);
        System.out.println(time);
        Request request= new Request("Sam",date.format(dTF),time.format(hr24), "Montgomery County", "Waiting");
                request.addUser(user);
        //user.addRequest(request);
        //requestRepository.save(request);

        userRepository.save(user);




        user = new FitnessUser();

        user.setFirstName("Sam");
        user.setLastName("Samson");
        user.setEmail("sam@sam.com");
        user.setContactNumber("301-333-5555");
        user.setEnabled(true);
        user.setUsername("sam");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(trainerRole));
        user.setFitnessLevel("Beginner");
        user.setArea("All");
        user.setGender("Male");
        user.setNeedOrSpecialty("Dance");


       date=LocalDate.parse("11/11/2017",dTF);
         time= LocalTime.parse("11:02",hr24);
        System.out.println(time);
         request= new Request("Sam",date.format(dTF),time.format(hr24), "Montgomery County", "Accepted");
        request.setSenderName("Bob");
        request.setAnswered(true);
        request.setReceiverAnswer("Accepted");
         request.addUser(user);

        user.addRequest(request);
        requestRepository.save(request);


        date=LocalDate.parse("11/11/2017",dTF);
        time= LocalTime.parse("12:30",hr24);
        System.out.println(time);
        request= new Request("Sam",date.format(dTF),time.format(hr24), "Montgomery County", "Waiting");
        request.setSenderName("Bob");
        request.setAnswered(false);
        request.addUser(user);

        user.addRequest(request);
        requestRepository.save(request);

        userRepository.save(user);



        user = new FitnessUser();

        user.setFirstName("Admin");
        user.setLastName("Admin");
        user.setEmail("admin@admin.com");
        user.setContactNumber("301-444-5555");
        user.setEnabled(true);
        user.setUsername("admin");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(trainerRole, adminRole));
        user.setFitnessLevel("Beginner");
        user.setArea("All");
        user.setGender("Male");
        user.setNeedOrSpecialty("Dance");
        userRepository.save(user);

    }
}
