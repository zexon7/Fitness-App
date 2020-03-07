package com.example.fitnessapp;

public class Record {
    private String User;
    private String Date;
    private  String FName;
    private Long FCal;

    public Record(){

    }
    public Record(String User, String Date, String FName, Long FCal){
        this.User = User;
        this.Date = Date;
        this.FName = FName;
        this.FCal = FCal;
    }

    public String getUser() {
        return User;
    }

    public String getDate() {
        return Date;
    }

    public String getFName() {
        return FName;
    }

    public Long getFCal() {
        return FCal;
    }
}


