package com.example.fitnessandnutritionapp;

import com.google.firebase.firestore.PropertyName;

public class FoodsLoggedModel {
    private String name;
    private String Calories;
    private String item_id;

    private FoodsLoggedModel(){}
    private FoodsLoggedModel(String name, String Calories, String item_id){
        this.name = name;
        this.Calories = Calories;
        this.item_id = item_id;
    }
    @PropertyName("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("Calories")
    public String getCalories() {
        return Calories;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    /*public void setCalories(String calories) {
        Calories = calories;
    }

     */


}
