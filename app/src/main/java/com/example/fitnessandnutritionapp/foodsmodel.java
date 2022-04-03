package com.example.fitnessandnutritionapp;

public class foodsmodel {
    private String name;
    public String item_id;
    public String verified;

    private foodsmodel(){}

    private foodsmodel(String name, String item_id, String verified){
        this.name = name;
        this.item_id = item_id;
        this.verified = verified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }
}
