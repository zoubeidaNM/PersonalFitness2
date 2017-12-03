package com.example.personalfitness;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<FitnessUser, Long>{
    FitnessUser findByUsername(String username);
    FitnessUser findByEmail(String email);
    Long countByEmail(String email);
    Long countByUsername(String username);
    Iterable<FitnessUser> findAllByUsernameContaining(String name);
    Iterable<FitnessUser> findAllByGenderContaining(String gender);
    Iterable<FitnessUser> findAllByAverageRatingContaining(int rating);

}
