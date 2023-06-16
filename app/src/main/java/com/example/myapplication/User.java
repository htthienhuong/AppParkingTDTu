package com.example.myapplication;

import com.google.firebase.database.Exclude;

public class User {
    public String fullName, day,month, year, email, gender, major;
    public Integer amount;

    public User(){

    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public User(String fullname, String day, String month, String year, String email, String gender, String major){

        this.fullName = fullname;
        this.day = day;
        this.month = month;
        this.year = year;
        this.email = email;
        this.gender = gender;
        this.major = major;
    }
    public User(String fullname, String day,String month,String year, String email,String gender, String major, Integer amount){
        this.fullName = fullname;
        this.day = day;
        this.month = month;
        this.year = year;
        this.email = email;
        this.gender = gender;
        this.major = major;
        this.amount = amount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Exclude
    public String getBirthday() {
        return String.format("%s/%s/%s", day, month, year);
    }

    @Exclude
    public void setBirthday(String birthday) {
        String[] parts = birthday
                .replaceAll("\\s+", "")
                .split("/");

        if (parts.length == 3) {
            this.day = parts[0];
            this.month = parts[1];
            this.year = parts[2];
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", day='" + day + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}