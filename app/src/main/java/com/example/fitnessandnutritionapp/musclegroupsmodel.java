package com.example.fitnessandnutritionapp;

public class musclegroupsmodel {
    private String name;
    public String item_id;

    private musclegroupsmodel(){}

    private musclegroupsmodel(String name,String item_id){
        this.name = name;
        this.item_id = item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }
}
