package com.example.personalfitness;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Request {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    @NotNull
    @NotEmpty
    private String receiverName;


    private String date;


     private String time;

    @NotNull
    @NotEmpty
    private String area;

    private int day;
    private String month;
    private int year;

    private int hours;
    private int minutes;


    private String status;


    private boolean answered;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    private String senderName;

    public String getReceiverAnswer() {
        return receiverAnswer;
    }

    public void setReceiverAnswer(String receiverAnswer) {
        this.receiverAnswer = receiverAnswer;
    }

    private String receiverAnswer;

    @ManyToMany(mappedBy = "requests")
    private Set<FitnessUser> users;

    public Request() {
        users=new HashSet<FitnessUser>();
    }

    public Request(String receiverName, String date, String time, String area, String status) {
        this.receiverName = receiverName;
        this.date = date;
        this.time = time;
        this.area = area;
        this.status = status;
        this.users=new HashSet<FitnessUser>();
    }

    public void processDate(){
        String dayStr;
        if(day<10) {
        dayStr="0"+day;
        }else{
            dayStr = ""+day;
        }

            date = dayStr+"/"+month+"/"+year;
        System.out.println(date);
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


}
