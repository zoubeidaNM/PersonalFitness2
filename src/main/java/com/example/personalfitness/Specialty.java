package com.example.personalfitness;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Specialty {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<FitnessUser> users;


    public Specialty() {
        users=new HashSet<FitnessUser>();
    }

     public Specialty(String name) {
        this.name = name;
        users=new HashSet<FitnessUser>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<FitnessUser> getUsers() {
        return users;
    }

    public void setUsers(Set<FitnessUser> users) {
        this.users = users;
    }

    public void addUser(FitnessUser user){
        users.add(user);
    }
}
