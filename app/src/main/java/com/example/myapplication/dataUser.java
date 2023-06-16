package com.example.myapplication;

public class dataUser {
    private String license;

    private String parking;

    private String check_out;


    private String check_in;

    public dataUser() {
    }

    public dataUser(String license, String parking, String check_out, String check_in) {
        this.license = license;
        this.parking = parking;
        this.check_out = check_out;
        this.check_in = check_in;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getCheck_out() {
        return check_out;
    }

    public void setCheck_out(String check_out) {
        this.check_out = check_out;
    }

    public String getCheck_in() {
        return check_in;
    }

    public void setCheck_in(String check_in) {
        this.check_in = check_in;
    }
}
