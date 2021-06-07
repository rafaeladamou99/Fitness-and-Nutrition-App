package com.example.fitnessandnutritionapp;

import java.util.List;

public class WorkoutsModel {

    private String name;
    private String photo;
    private List<String> exercises;
    private String category;
    private String duration;
    private String level;
    private List<String> rest;
    private List<String> setsrepsrest;
    private List<String> tags;

    private WorkoutsModel(){}

    private WorkoutsModel(String name, String photo, List<String> exercises, String category, String duration, String level, List<String> rest, List<String> setsrepsrest, List<String> tags){
        this.name = name;
        this.photo = photo;
        this.exercises = exercises;
        this.category = category;
        this.duration = duration;
        this.level = level;
        this.rest = rest;
        this.setsrepsrest = setsrepsrest;
        this.tags = tags;
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

    public List<String> getExercises() {
        return exercises;
    }

    public void setExercises(List<String> exercises) {
        this.exercises = exercises;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<String> getRest() {
        return rest;
    }

    public void setRest(List<String> rest) {
        this.rest = rest;
    }

    public List<String> getSetsrepsrest() {
        return setsrepsrest;
    }

    public void setSetsrepsrest(List<String> setsrepsrest) {
        this.setsrepsrest = setsrepsrest;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}