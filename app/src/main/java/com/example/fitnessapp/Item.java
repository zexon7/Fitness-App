package com.example.fitnessapp;

public class Item {
    private String FName;
    private Long FCal;

    public Item(){
        //empty constructor needed
    }

    public Item(String FName, Long FCal){
        this.FName = FName;
        this.FCal = FCal;
    }

    public String getFName() {
        return FName;
    }

    public Long getFCal() {
        return FCal;
    }
}
