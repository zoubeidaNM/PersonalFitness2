package com.example.personalfitness;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
public class FitnessUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @NotNull
    @NotEmpty
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @NotEmpty
    @Column(name = "last_name")
    private String lastName;


    @NotNull
    @NotEmpty
    @Column(name = "email")
    private String email;

    @NotNull
    @NotEmpty
    @Column(name = "contact")
    private String contactNumber;

//    @NotNull
//    @NotEmpty
//    @Column(name = "fitness_level")
//    private String fitnessLevel;


    // Trainer will choose ALL.

    @Column(name = "area")
    private String area;

    @Column(name = "gender")
    private String gender;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "password")
    @NotEmpty
    @NotNull
    @Size(min = 2, max = 20)
    private String password;

    @Column(name = "username", unique = true)
    @NotEmpty
    @NotNull
    @Size(min = 2, max = 20)
    private String username;

    private String headshot;

    private boolean userRequestFlag;

    private boolean trainerRequestFlag;

    private int averageRating;

    private boolean suspended;

    ArrayList<String> ratableUserNames;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<UserRole> roles;


    @ManyToMany
    public Set<Comment> comments;

    @ManyToMany
    private Set<Request> requests;

    @ManyToMany
    private Set<Request> trainerDeclinedRequests;

    @ManyToMany(mappedBy = "users",fetch = FetchType.LAZY)
    private Set<Specialty> specialties;

    @ManyToMany(mappedBy = "fitnessLevelUsers",fetch = FetchType.LAZY)
    private Set<FitnessLevel> fitnessLevels;


    public FitnessUser() {
        roles = new HashSet<UserRole>();
        requests = new HashSet<Request>();
        specialties = new HashSet<Specialty>();
        fitnessLevels = new HashSet<FitnessLevel>();

        trainerDeclinedRequests = new HashSet<Request>();
        ratableUserNames = new ArrayList<String>();
        averageRating = 0;

        headshot = "/images/pic01.jpg";

        userRequestFlag = false;
        trainerRequestFlag = false;
        suspended = false;

    }

    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

//    public String getFitnessLevel() {
//        return fitnessLevel;
//    }
//
//    public void setFitnessLevel(String fitnessLevel) {
//        this.fitnessLevel = fitnessLevel;
//    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Collection<UserRole> roles) {
        this.roles = roles;
    }


    public void addSpecialty(Specialty specialty) {
        specialties.add(specialty);
    }

    public void addFitnessLevel(FitnessLevel fitnessLevel) {
        fitnessLevels.add(fitnessLevel);
    }
    public void addRequest(Request request) {
        requests.add(request);
    }

    public String getHeadshot() {
        return headshot;
    }

    public void setHeadshot(String headshot) {
        this.headshot = headshot;
    }

    public boolean isUserRequestFlag() {
        return userRequestFlag;
    }

    public void setUserRequestFlag(boolean userRequestFlag) {
        this.userRequestFlag = userRequestFlag;
    }

    public boolean isTrainerRequestFlag() {
        return trainerRequestFlag;
    }

    public void setTrainerRequestFlag(boolean trainerRequestFlag) {
        this.trainerRequestFlag = trainerRequestFlag;
    }

    public Set<Request> getTrainerDeclinedRequests() {
        return trainerDeclinedRequests;
    }

    public void setTrainerDeclinedRequests(Set<Request> trainerDeclinedRequests) {
        this.trainerDeclinedRequests = trainerDeclinedRequests;
    }

    public int getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(int averageRating) {
        this.averageRating = averageRating;
    }

    public void discardDeniedRequest(Request request) {
        requests.remove(request);
        trainerDeclinedRequests.add(request);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void computeAverageRating() {
        int commentSum = 0;

        for (Comment c : comments) {
            commentSum = commentSum + c.getRating();
        }
        averageRating = commentSum / comments.size();

    }

    public Set<Specialty> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(Set<Specialty> specialties) {
        this.specialties = specialties;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<String> getRatableUserNames() {
        return ratableUserNames;
    }

    public void setRatableUserNames(ArrayList<String> ratableUserNames) {
        this.ratableUserNames = ratableUserNames;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public void addRatableUserName(String name) {
        ratableUserNames.add(name);
    }

    public Set<FitnessLevel> getFitnessLevels() {
        return fitnessLevels;
    }

    public void setFitnessLevels(Set<FitnessLevel> fitnessLevels) {
        this.fitnessLevels = fitnessLevels;
    }
}
