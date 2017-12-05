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

    @Autowired
    FitnessLevelRepository fitnessLevels;


    @Override
    public void run(String... strings) throws Exception {
        System.out.println("Loading data . . .");

        roleRepository.save(new UserRole("USER"));
        roleRepository.save(new UserRole("TRAINER"));
        roleRepository.save(new UserRole("ADMIN"));
        roleRepository.save(new UserRole("SUSPENDED"));

        UserRole userRole = roleRepository.findByRole("USER");
        UserRole trainerRole = roleRepository.findByRole("TRAINER");
        UserRole adminRole = roleRepository.findByRole("ADMIN");

        Area area = new Area("Montgomery County");
        areas.save(area);
        area = new Area("Frederick County");
        areas.save(area);
        area = new Area("Prince George's County");
        areas.save(area);
        area = new Area("All");
        areas.save(area);

        FitnessLevel fitnessLevel = new FitnessLevel( "Beginner");
        fitnessLevels.save(fitnessLevel);
        fitnessLevel = new FitnessLevel( "Intermediate");
        fitnessLevels.save(fitnessLevel);
        fitnessLevel = new FitnessLevel( "Advanced");
        fitnessLevels.save(fitnessLevel);
        fitnessLevel = new FitnessLevel( "All");
        fitnessLevels.save(fitnessLevel);


        Specialty specialty = new Specialty("Weight Training");
        specialties.save(specialty);
        specialty = new Specialty("Martial Arts");
        specialties.save(specialty);
        specialty = new Specialty("Water Sports");
        specialties.save(specialty);
        specialty = new Specialty("Aerobics");
        specialties.save(specialty);
        specialty = new Specialty("Dance");
        specialties.save(specialty);



        //        User 1
        FitnessUser user = new FitnessUser();



        user.setFirstName("Bob");
        user.setLastName("Bobberson");
        user.setEmail("bob@bob.com");
        user.setContactNumber("301-555-5555");
        user.setEnabled(true);
        user.setUsername("bob");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(userRole));
        user.setArea("Frederick County");
        user.setGender("Male");
        user.setHeadshot("/images/bob.jpg");
        userRepository.save(user);
        DateTimeFormatter dTF = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter hr24 = DateTimeFormatter.ofPattern("kk:m");



        specialty = specialties.findByName("Weight Training");
        specialty.addUser(user);
        specialties.save(specialty);
        user.addSpecialty(specialty);

        fitnessLevel = fitnessLevels.findByName("Beginner");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);

        userRepository.save(user);

//Trainer 1





        user = new FitnessUser();

        user.setFirstName("Sam");
        user.setLastName("Samson");
        user.setEmail("sam@sam.com");
        user.setContactNumber("301-333-5555");
        user.setEnabled(true);
        user.setUsername("sam");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(trainerRole));
        user.setArea("All");
        user.setGender("Male");
        user.setHeadshot("/images/sam.jpg");

        userRepository.save(user);


        specialty = specialties.findByName("Weight Training");
        specialty.addUser(user);
        specialties.save(specialty);
        user .addSpecialty(specialty);
        specialty = specialties.findByName("Aerobics");
        specialty.addUser(user);
        specialties.save(specialty);
        user .addSpecialty(specialty);
        specialty = specialties.findByName("Dance");
        specialty.addUser(user);
        specialties.save(specialty);
        user .addSpecialty(specialty);

        fitnessLevel = fitnessLevels.findByName("Advanced");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);
        fitnessLevel = fitnessLevels.findByName("Intermediate");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);

        userRepository.save(user);

        user = new FitnessUser();


//        Trainer 2

        user.setFirstName("Shaun");
        user.setLastName("Roberts");
        user.setEmail("shaun@personalfit.com");
        user.setContactNumber("301-444-5555");
        user.setEnabled(true);
        user.setUsername("Shaun11");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(trainerRole));
        user.setArea("All");
        user.setGender("Male");
        user.setAverageRating(4);
user.setHeadshot("/images/shaun.jpg");
        userRepository.save(user);

        specialty = specialties.findByName("Weight Training");
        specialty.addUser(user);
        specialties.save(specialty);
        user .addSpecialty(specialty);
        specialty = specialties.findByName("Aerobics");
        specialty.addUser(user);
        specialties.save(specialty);
        user .addSpecialty(specialty);

        fitnessLevel = fitnessLevels.findByName("All");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);

        userRepository.save(user);


//        Trainer 3
        user = new FitnessUser();

        user.setFirstName("Tony");
        user.setLastName("Stevenson");
        user.setEmail("Tony@personalfit.com");
        user.setContactNumber("301-444-5566");
        user.setEnabled(true);
        user.setUsername("Tony23");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(trainerRole));
        user.setArea("All");
        user.setGender("Male");
        user.setAverageRating(2);
        user.setHeadshot("/images/tony.jpg");
        userRepository.save(user);

        fitnessLevel = fitnessLevels.findByName("Beginner");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);

        fitnessLevel = fitnessLevels.findByName("Intermediate");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);

        specialty = specialties.findByName("Martial Arts");
        specialty.addUser(user);
        specialties.save(specialty);
        user .addSpecialty(specialty);

        userRepository.save(user);



//        Trainer 4
        user = new FitnessUser();

        user.setFirstName("Camille");
        user.setLastName("James");
        user.setEmail("Camille@personalfit.com");
        user.setContactNumber("301-224-5566");
        user.setEnabled(true);
        user.setUsername("Camille18");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(trainerRole));
        user.setArea("All");
        user.setGender("Female");
        user.setAverageRating(5);
        user.setHeadshot("/images/camille.jpg");
        userRepository.save(user);

        fitnessLevel = fitnessLevels.findByName("Beginner");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);

        fitnessLevel = fitnessLevels.findByName("Advanced");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);

        specialty = specialties.findByName("Water Sports");
        specialty.addUser(user);
        specialties.save(specialty);
        user.addSpecialty(specialty);

        specialty = specialties.findByName("Weight Training");
        specialty.addUser(user);
        specialties.save(specialty);
        user.addSpecialty(specialty);

        userRepository.save(user);



        //        Trainer 5
        user = new FitnessUser();

        user.setFirstName("Vince");
        user.setLastName("Chen");
        user.setEmail("Vince@personalfit.com");
        user.setContactNumber("301-111-5566");
        user.setEnabled(true);
        user.setUsername("Vince88");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(trainerRole));
        user.setArea("All");
        user.setGender("Male");
        user.setAverageRating(5);
        user.setHeadshot("/images/vince.jpg");
        userRepository.save(user);


        fitnessLevel = fitnessLevels.findByName("Advanced");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);

        specialty = specialties.findByName("Martial Arts");
        specialty.addUser(user);
        specialties.save(specialty);
        user.addSpecialty(specialty);

        specialty = specialties.findByName("Weight Training");
        specialty.addUser(user);
        specialties.save(specialty);
        user.addSpecialty(specialty);

        specialty = specialties.findByName("Dance");
        specialty.addUser(user);
        specialties.save(specialty);
        user.addSpecialty(specialty);

        userRepository.save(user);


//        Admin 2

        user = new FitnessUser();

        user.setFirstName("Gilbert");
        user.setLastName("Smith");
        user.setEmail("gilbert@personalfit.com");
        user.setContactNumber("301-234-5678");
        user.setEnabled(true);
        user.setUsername("gsmith");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(trainerRole, adminRole));
        user.setArea("All");
        user.setGender("Male");
        user.setAverageRating(4);
        user.setHeadshot("/images/gilbert.jpg");

        userRepository.save(user);

        fitnessLevel = fitnessLevels.findByName("Advanced");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);
        fitnessLevel = fitnessLevels.findByName("Intermediate");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);


        specialty = specialties.findByName("Aerobics");
        specialty.addUser(user);
        specialties.save(specialty);
        user .addSpecialty(specialty);

        specialty = specialties.findByName("Water Sports");
        specialty.addUser(user);
        specialties.save(specialty);
        user .addSpecialty(specialty);

        userRepository.save(user);


        //        Admin 1

        user = new FitnessUser();

        user.setFirstName("Michelle");
        user.setLastName("O'Keefe");
        user.setEmail("michelle@personalfit.com");
        user.setContactNumber("301-444-7777");
        user.setEnabled(true);
        user.setUsername("michelle");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(trainerRole, adminRole));
        user.setArea("All");
        user.setGender("Female");
        user.setAverageRating(5);
        user.setHeadshot("/images/michelle.jpg");


        userRepository.save(user);

        fitnessLevel = fitnessLevels.findByName("Advanced");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);
        fitnessLevel = fitnessLevels.findByName("Intermediate");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);


        specialty = specialties.findByName("Water Sports");
        specialty.addUser(user);
        specialties.save(specialty);
        user .addSpecialty(specialty);

        specialty = specialties.findByName("Martial Arts");
        specialty.addUser(user);
        specialties.save(specialty);
        user .addSpecialty(specialty);

        userRepository.save(user);



//        User 2
        user = new FitnessUser();

        user.setFirstName("Danielle");
        user.setLastName("Guthrie");
        user.setEmail("danielle@personalfit.com");
        user.setContactNumber("301-444-3366");
        user.setEnabled(true);
        user.setUsername("dg1234");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(userRole));
        user.setArea("Montgomery County");
        user.setGender("Female");
        user.setAverageRating(3);
        user.setHeadshot("/images/danielle.jpg");

        userRepository.save(user);



        fitnessLevel = fitnessLevels.findByName("Advanced");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);


        specialty = specialties.findByName("Dance");
        specialty.addUser(user);
        specialties.save(specialty);
        user .addSpecialty(specialty);

        specialty = specialties.findByName("Weight Training");
        specialty.addUser(user);
        specialties.save(specialty);
        user .addSpecialty(specialty);

        userRepository.save(user);


//        User 3
        user = new FitnessUser();

        user.setFirstName("Nathalie");
        user.setLastName("Gates");
        user.setEmail("nathalie@personalfit.com");
        user.setContactNumber("301-444-8899");
        user.setEnabled(true);
        user.setUsername("nathalie03");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(userRole));
        user.setArea("All");
        user.setGender("Female");
        user.setAverageRating(4);
        user.setHeadshot("/images/nathalie.jpg");
        userRepository.save(user);


        fitnessLevel = fitnessLevels.findByName("Beginner");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);


        specialty = specialties.findByName("Martial Arts");
        specialty.addUser(user);
        specialties.save(specialty);
        user.addSpecialty(specialty);

        userRepository.save(user);


//        User 4
        user = new FitnessUser();

        user.setFirstName("Blanca");
        user.setLastName("Sandoval");
        user.setEmail("blanca@personalfit.com");
        user.setContactNumber("301-333-8899");
        user.setEnabled(true);
        user.setUsername("bsandoval21");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(userRole));
        user.setArea("Prince George's County");
        user.setGender("Female");
        user.setHeadshot("/images/blanca.jpg");
        userRepository.save(user);


        fitnessLevel = fitnessLevels.findByName("Beginner");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);


        specialty = specialties.findByName("Dance");
        specialty.addUser(user);
        specialties.save(specialty);
        user.addSpecialty(specialty);


        specialty = specialties.findByName("Martial Arts");
        specialty.addUser(user);
        specialties.save(specialty);
        user.addSpecialty(specialty);

        userRepository.save(user);



//User 5

        user = new FitnessUser();
        user.setFirstName("Jim");
        user.setLastName("Jimson");
        user.setEmail("jim@jim.com");
        user.setContactNumber("301-111-5555");
        user.setEnabled(true);
        user.setUsername("jim");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(userRole));
        user.setArea("Frederick County");
        user.setGender("Male");
        user.setAverageRating(3);
        user.setHeadshot("/images/jim.jpg");
        userRepository.save(user);


        fitnessLevel = fitnessLevels.findByName("Intermediate");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);


        specialty = specialties.findByName("Water Sports");
        specialty.addUser(user);
        specialties.save(specialty);
        user .addSpecialty(specialty);

        userRepository.save(user);

        //User 6

        user = new FitnessUser();
        user.setFirstName("Xavier");
        user.setLastName("Marcus");
        user.setEmail("xavier@personalfit.com");
        user.setContactNumber("301-100-5555");
        user.setEnabled(true);
        user.setUsername("xmarcus11");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(userRole));
        user.setArea("Prince George's County");
        user.setGender("Male");
        user.setAverageRating(3);
        user.setHeadshot("/images/xavier.jpg");
        userRepository.save(user);


        fitnessLevel = fitnessLevels.findByName("Intermediate");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);


        specialty = specialties.findByName("Dance");
        specialty.addUser(user);
        specialties.save(specialty);
        user.addSpecialty(specialty);


        specialty = specialties.findByName("Weight Training");
        specialty.addUser(user);
        specialties.save(specialty);
        user.addSpecialty(specialty);
        userRepository.save(user);


        //User 7

        user = new FitnessUser();
        user.setFirstName("Sonia");
        user.setLastName("Gupta");
        user.setEmail("sgupta@personalfit.com");
        user.setContactNumber("301-100-3444");
        user.setEnabled(true);
        user.setUsername("sgupta");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(userRole));
        user.setArea("Montgomery County");
        user.setGender("Female");
        user.setAverageRating(4);
        user.setHeadshot("/images/sonia.jpg");
        userRepository.save(user);


        fitnessLevel = fitnessLevels.findByName("Advanced");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);



        specialty = specialties.findByName("Martial Arts");
        specialty.addUser(user);
        specialties.save(specialty);
        user.addSpecialty(specialty);
        userRepository.save(user);



        //User 8

        user = new FitnessUser();
        user.setFirstName("Miriam");
        user.setLastName("Levy");
        user.setEmail("mlevy@personalfit.com");
        user.setContactNumber("301-100-3222");
        user.setEnabled(true);
        user.setUsername("mlevy");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(userRole));
        user.setArea("Montgomery County");
        user.setGender("Female");
user.setHeadshot("/images/miriam.jpg");
        userRepository.save(user);


        fitnessLevel = fitnessLevels.findByName("Beginner");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);



        specialty = specialties.findByName("Water Sports");
        specialty.addUser(user);
        specialties.save(specialty);
        user.addSpecialty(specialty);
        userRepository.save(user);

        specialty = specialties.findByName("Weight Training");
        specialty.addUser(user);
        specialties.save(specialty);
        user.addSpecialty(specialty);
        userRepository.save(user);



        //User 9

        user = new FitnessUser();
        user.setFirstName("Lydia");
        user.setLastName("Langston");
        user.setEmail("llangston@personalfit.com");
        user.setContactNumber("301-100-3111");
        user.setEnabled(true);
        user.setUsername("llangston");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(userRole));
        user.setArea("Montgomery County");
        user.setGender("Female");
        user.setHeadshot("/images/lydia.jpg");
        userRepository.save(user);


        fitnessLevel = fitnessLevels.findByName("Intermediate");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);


        specialty = specialties.findByName("Martial Arts");
        specialty.addUser(user);
        specialties.save(specialty);
        user.addSpecialty(specialty);
        userRepository.save(user);

        specialty = specialties.findByName("Aerobics");
        specialty.addUser(user);
        specialties.save(specialty);
        user.addSpecialty(specialty);
        userRepository.save(user);



        //User 10

        user = new FitnessUser();
        user.setFirstName("Marc");
        user.setLastName("Taylor");
        user.setEmail("mtaylor@personalfit.com");
        user.setContactNumber("301-100-3111");
        user.setEnabled(true);
        user.setUsername("mtaylor");
        user.setPassword("pass");
        user.setRoles(Arrays.asList(userRole));
        user.setArea("Montgomery County");
        user.setGender("Male");
user.setHeadshot("/images/marc.jpg");
        userRepository.save(user);


        fitnessLevel = fitnessLevels.findByName("Beginner");
        fitnessLevel.addUser(user);
        fitnessLevels.save(fitnessLevel);
        user.addFitnessLevel(fitnessLevel);


        specialty = specialties.findByName("Aerobics");
        specialty.addUser(user);
        specialties.save(specialty);
        user.addSpecialty(specialty);
        userRepository.save(user);

        specialty = specialties.findByName("Dance");
        specialty.addUser(user);
        specialties.save(specialty);
        user.addSpecialty(specialty);
        userRepository.save(user);
    }
}
