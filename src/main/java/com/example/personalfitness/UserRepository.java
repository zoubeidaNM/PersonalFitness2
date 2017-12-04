package com.example.personalfitness;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<FitnessUser, Long>{
    FitnessUser findByUsername(String username);
    FitnessUser findByEmail(String email);
    Long countByEmail(String email);
    Long countByUsername(String username);
    Iterable<FitnessUser> findAllByUsernameContainingIgnoreCase(String name);
    Iterable<FitnessUser> findAllByGenderIgnoreCase(String gender);
    Iterable<FitnessUser> findAllByAverageRating(int rating);

}
