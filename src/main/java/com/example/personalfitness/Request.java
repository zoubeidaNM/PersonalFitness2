package com.example.personalfitness;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Request {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;


    @NotNull
    @NotEmpty
    private String receiverName;

    @NotNull
    @NotEmpty
    private String area;

    private String senderName;
    private String receiverAnswer; //Accepted, Denied
    private String status; //Waiting, Accepted, Conflict
    private boolean answered;
    private boolean showTrainer;

    private String date;
    private String time;
    private int day;
    private String month;
    private int year;
    private int hours;
    private int minutes;

    @ManyToMany(mappedBy = "requests")
    private Set<FitnessUser> users;

    @ManyToMany(mappedBy = "trainerDeclinedRequests")
    private Set<FitnessUser> decliningtrainers;

    public Request() {
        users=new HashSet<FitnessUser>();
        showTrainer =true;
    }

    public Request(String receiverName, String date, String time, String area, String status) {
        this.receiverName = receiverName;
        this.date = date;
        this.time = time;
        this.area = area;
        this.status = status;
        this.users=new HashSet<FitnessUser>();
        showTrainer = true;
    }

    public boolean processDate(){
        String dayStr;
        boolean validDate=false;
        if(day<10) {
        dayStr="0"+day;
        }else{
            dayStr = ""+day;
        }

            date = dayStr+"/"+month+"/"+year;
        DateTimeFormatter dTF = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter longFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy");

        LocalDate formattedDate=null;
        try{
            formattedDate = LocalDate.parse(date,dTF);
             date = formattedDate.format(longFormat);
             validDate = true;
        }catch(Exception e)
        {
            System.out.println(e.toString());
            validDate = false;
        }

        System.out.println(date);
        return validDate;
    }

    public boolean isDateInTheFuture() {
        DateTimeFormatter longFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        LocalDate formattedDate=null;
        boolean isIntheFuture = false;
        try{
            formattedDate = LocalDate.parse(date,longFormat);

            if(formattedDate.isAfter(LocalDate.now())) {
                isIntheFuture= true;
            }else{
                isIntheFuture=  false;
            }

        }catch(Exception e)
        {
            System.out.println(e.toString());
        }
        return isIntheFuture;
    }

    public void processTime(){
        String hoursStr ="";
        String minutesStr ="";

        if(hours<10){
            hoursStr = "0"+hours;
        }else {
        hoursStr= ""+hours;
        }
        if(minutes<10){
            minutesStr = "0"+minutes;
        }else {
            minutesStr=""+minutes;
        }

        time = hoursStr+":"+minutesStr;

        System.out.println(time);

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Set<FitnessUser> getDecliningtrainers() {
        return decliningtrainers;
    }

    public void setDecliningtrainers(Set<FitnessUser> decliningtrainers) {
        this.decliningtrainers = decliningtrainers;
    }

    public String getReceiverAnswer() {
        return receiverAnswer;
    }

    public void setReceiverAnswer(String receiverAnswer) {
        this.receiverAnswer = receiverAnswer;
    }

    public boolean isShowTrainer() {
        return showTrainer;
    }

    public void setShowTrainer(boolean showTrainer) {
        this.showTrainer = showTrainer;
    }

    public void addDecliningTrainer(FitnessUser trainer){
        decliningtrainers.add(trainer);
    }
}
