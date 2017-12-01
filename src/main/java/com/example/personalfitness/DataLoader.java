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

    @Autowired
    NameRepository areas;

    @Autowired
    NameRepository specialties;



    @Override
    public void run(String... strings) throws Exception {
        System.out.println("Loading data . . .");

        roleRepository.save(new UserRole("USER"));
        roleRepository.save(new UserRole("TRAINER"));
        roleRepository.save(new UserRole("ADMIN"));

        UserRole userRole = roleRepository.findByRole("USER");
        UserRole trainerRole = roleRepository.findByRole("TRAINER");
        UserRole adminRole = roleRepository.findByRole("ADMIN");

        Name name1= new Name("Montgomery County");
        Name name2= new Name("Frederick County");
        Name name3= new Name("Prince George's County");
        Name name4= new Name("All");

        areas.save(name1);
        areas.save(name2);
        areas.save(name3);
        areas.save(name4);

        name1=new Name("Weight Training");
        name2=new Name("Martial Arts ");
        name3=new Name("Water Sports");
        name4=new Name("Aerobics");
        Name name5=new Name("Dance");

//        specialties.save(name1);
//        specialties.save(name2);
//        specialties.save(name3);
//        specialties.save(name4);
//        specialties.save(name5);


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
        user.setFitnessLevel("Intermediate");
        user.setArea("Frederick County");
        user.setGender("Male");
        user.setNeedOrSpecialty("Dance");
        DateTimeFormatter dTF = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter hr24 = DateTimeFormatter.ofPattern("kk:m");


        userRepository.save(user);

        user = new FitnessUser();
        user.setFirstName("Jim");
        user.setLastName("Jimson");
        user.setEmail("jim@jim.com");
        user.setContactNumber("301-111-5555");
        user.setEnabled(true);
        user.setUsername("jim");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(userRole));
        user.setFitnessLevel("Beginner");
        user.setArea("Frederick County");
        user.setGender("Male");
        user.setNeedOrSpecialty("Weight Training");


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
        user.setFitnessLevel("Advanced");
        user.setArea("All");
        user.setGender("Male");
        user.setNeedOrSpecialty("Martial Arts");

        userRepository.save(user);






        user = new FitnessUser();

        user.setFirstName("Shaun");
        user.setLastName("Roberts");
        user.setEmail("shaun@personalfit.com");
        user.setContactNumber("301-444-5555");
        user.setEnabled(true);
        user.setUsername("Shaun11");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(trainerRole));
        user.setFitnessLevel("Advanced");
        user.setArea("All");
        user.setGender("Male");
        user.setNeedOrSpecialty("Martial Arts");
        userRepository.save(user);

        user = new FitnessUser();

        user.setFirstName("Tony");
        user.setLastName("Stevenson");
        user.setEmail("Tony@personalfit.com");
        user.setContactNumber("301-444-5566");
        user.setEnabled(true);
        user.setUsername("Tony23");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(trainerRole));
        user.setFitnessLevel("Beginner");
        user.setArea("All");
        user.setGender("Male");
        user.setNeedOrSpecialty("Aerobics");
        userRepository.save(user);

        user = new FitnessUser();

        user.setFirstName("Michelle");
        user.setLastName("Okeefe");
        user.setEmail("michelle@personalfit.com");
        user.setContactNumber("301-444-7777");
        user.setEnabled(true);
        user.setUsername("MichelleO");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(trainerRole));
        user.setFitnessLevel("Intermediate");
        user.setArea("All");
        user.setGender("Female");
        user.setNeedOrSpecialty("Aerobics");
        userRepository.save(user);

        user = new FitnessUser();

        user.setFirstName("Danielle");
        user.setLastName("guthrie");
        user.setEmail("danielle@personalfit.com");
        user.setContactNumber("301-444-3366");
        user.setEnabled(true);
        user.setUsername("Danielle");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(trainerRole));
        user.setFitnessLevel("Beginner");
        user.setArea("All");
        user.setGender("Female");
        user.setNeedOrSpecialty("Dance");
        userRepository.save(user);

        user = new FitnessUser();

        user.setFirstName("nathalie");
        user.setLastName("gates");
        user.setEmail("nathalie@personalfit.com");
        user.setContactNumber("301-444-8899");
        user.setEnabled(true);
        user.setUsername("nathalie03");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(trainerRole));
        user.setFitnessLevel("Beginner");
        user.setArea("All");
        user.setGender("Male");
        user.setNeedOrSpecialty("Water Sports");
        userRepository.save(user);



    }
}
