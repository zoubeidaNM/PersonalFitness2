package com.example.personalfitness;

import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<UserRole, Long>{

    UserRole findByRole(String Role);

}
