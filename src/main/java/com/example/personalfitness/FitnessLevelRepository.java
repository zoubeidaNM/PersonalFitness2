package com.example.personalfitness;

import org.springframework.data.repository.CrudRepository;

public interface FitnessLevelRepository extends CrudRepository<FitnessLevel, Long> {
    Iterable<FitnessLevel> findAllByNameContainingIgnoreCase(String name);
    FitnessLevel findByName(String Name);
}
