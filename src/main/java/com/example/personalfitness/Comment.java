package com.example.personalfitness;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Comment {
    public Comment() {
        users=new HashSet<FitnessUser>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public Date getPosteddate() {
        return posteddate;
    }

    public void setPosteddate(Date posteddate) {
        this.posteddate = posteddate;
    }

    public String getSentby() {
        return sentby;
    }

    public void setSentby(String sentby) {
        this.sentby = sentby;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @NotNull
    @NotEmpty
    private String content;


//    @ManyToOne(fetch=FetchType.EAGER)
//    @JoinColumn(name="user_id")
//    @ManyToMany(mappedBy="comments")
//    private FitnessUser user;


    public Set<FitnessUser> getUsers() {
        return users;
    }

    public void setUsers(Set<FitnessUser> users) {
        this.users = users;
    }

    @ManyToMany(mappedBy="comments")
    private Set<FitnessUser> users;

    private Date posteddate;


    private String sentby;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;


    private String trainerName;

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }



    @NotNull
    private int rating;

    public void addUser(FitnessUser user){users.add(user);}
}