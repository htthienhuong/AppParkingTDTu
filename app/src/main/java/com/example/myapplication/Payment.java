package com.example.myapplication;

public class Payment {
    private Integer amount;

    private String parking;

    private String check_out;



    public Payment() {
    }

    public Payment(Integer amount, String parking, String check_out ) {
        this.amount = amount;
        this.parking = parking;
        this.check_out = check_out;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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
}
