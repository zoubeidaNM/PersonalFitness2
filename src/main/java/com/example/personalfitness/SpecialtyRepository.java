package com.example.personalfitness;

import org.springframework.data.repository.CrudRepository;

public interface SpecialtyRepository extends CrudRepository<Specialty, Long>{
    Specialty findByName(String name);
    Iterable<Specialty> findAllByNameContaining(String name);
}
