package com.example.personalfitness;

import org.springframework.data.repository.CrudRepository;

public interface FitnessLevelRepository extends CrudRepository<FitnessLevel, Long> {
    Iterable<FitnessLevel> findAllByNameContainingIgnoreCase(String name);
    Iterable<FitnessLevel> findAllByNameContainingIgnoreCaseOrNameContainingIgnoreCase(String name1, String name2);
    FitnessLevel findByName(String Name);
}
