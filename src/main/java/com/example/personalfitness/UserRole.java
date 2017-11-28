package com.example.personalfitness;


import javax.persistence.*;
import java.util.Collection;

@javax.persistence.Entity
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique=true)
    private String role;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Collection<FitnessUser> users;

    public UserRole() {
    }

    public UserRole(String role) {
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Collection<FitnessUser> getUsers() {
        return users;
    }

    public void setUsers(Collection<FitnessUser> users) {
        this.users = users;
    }
}
