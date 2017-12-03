package com.example.personalfitness;

import org.springframework.data.repository.CrudRepository;

public interface AreaRepository extends CrudRepository<Area, Long> {
    Area findByName(String Name);
}
