package com.example.personalfitness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public FitnessUser findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public FitnessUser findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Long countByEmail(String email){
        return userRepository.countByEmail(email);
    }

    public void saveUser(FitnessUser user) {
        user.setRoles((Arrays.asList(roleRepository.findByRole("USER"))));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void saveTrainer(FitnessUser user) {
        user.setRoles((Arrays.asList(roleRepository.findByRole("TRAINER"))));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void saveAdmin(FitnessUser user){
        user.setRoles((Arrays.asList(roleRepository.findByRole("ADMIN"))));
        user.setEnabled(true);
        userRepository.save(user);

    }


}
