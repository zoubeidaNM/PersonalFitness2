package com.example.personalfitness;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class FitnessLevel {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<FitnessUser> fitnessLevelUsers;

    public FitnessLevel() {
        fitnessLevelUsers=new HashSet<FitnessUser>();
    }

    public FitnessLevel(String name) {
        this.name = name;
        fitnessLevelUsers=new HashSet<FitnessUser>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<FitnessUser> getFitnessLevelUsers() {
        return fitnessLevelUsers;
    }

    public void setFitnessLevelUsers(Set<FitnessUser> fitnessLevelUsers) {
        this.fitnessLevelUsers = fitnessLevelUsers;
    }

    public void addUser(FitnessUser user){
        fitnessLevelUsers.add(user);
    }

}
