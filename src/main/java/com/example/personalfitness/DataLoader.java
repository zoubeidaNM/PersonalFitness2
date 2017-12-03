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
    AreaRepository areas;

    @Autowired
    SpecialtyRepository specialties;



    @Override
    public void run(String... strings) throws Exception {
        System.out.println("Loading data . . .");

        roleRepository.save(new UserRole("USER"));
        roleRepository.save(new UserRole("TRAINER"));
        roleRepository.save(new UserRole("ADMIN"));

        UserRole userRole = roleRepository.findByRole("USER");
        UserRole trainerRole = roleRepository.findByRole("TRAINER");
        UserRole adminRole = roleRepository.findByRole("ADMIN");

        Area area = new Area("All");
        areas.save(area);
        area = new Area("Montgomery County");
        areas.save(area);
        area = new Area("Frederick County");
        areas.save(area);
        area = new Area("Prince George's County");
        areas.save(area);


        Specialty specialty = new Specialty("Weight Training");
        specialties.save(specialty);
        specialty = new Specialty("Martial Arts ");
        specialties.save(specialty);
        specialty = new Specialty("Water Sports");
        specialties.save(specialty);
        specialty = new Specialty("Aerobics");
        specialties.save(specialty);
        specialty = new Specialty("Dance");
        specialties.save(specialty);


        FitnessUser user = new FitnessUser();

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

        user.getSpecialties().add(specialties.findByName("Dance"));
        user.getSpecialties().add(specialties.findByName("Aerobics"));

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
        userRepository.save(user);

        user = new FitnessUser();

        user.setFirstName("Michelle");
        user.setLastName("Okeefe");
        user.setEmail("michelle@personalfit.com");
        user.setContactNumber("301-444-7777");
        user.setEnabled(true);
        user.setUsername("michelleO");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(trainerRole, adminRole));
        user.setFitnessLevel("Intermediate");
        user.setArea("All");
        user.setGender("Female");
        userRepository.save(user);

        user = new FitnessUser();

        user.setFirstName("danielle");
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
        userRepository.save(user);



    }
}
