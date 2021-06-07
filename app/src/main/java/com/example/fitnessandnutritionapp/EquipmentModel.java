package com.example.fitnessandnutritionapp;

public class EquipmentModel {

    private String name;
    private String photo;
    private String item_id;

    private EquipmentModel(){}

    private EquipmentModel(String name, String photo){
        this.name = name;
        this.photo = photo;
        this.item_id = item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }
}
