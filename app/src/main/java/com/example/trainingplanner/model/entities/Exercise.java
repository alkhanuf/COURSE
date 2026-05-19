package com.example.trainingplanner.model.entities;

public class Exercise {
    private long id;
    private String name;
    private double baseWeight;

    public Exercise(String name, double baseWeight) {
        this.name = name;
        this.baseWeight = baseWeight;
    }
    public Exercise(long id, String name, double baseWeight) {
        this.id = id;
        this.name = name;
        this.baseWeight = baseWeight;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getBaseWeight() { return baseWeight; }
    public void setBaseWeight(double baseWeight) { this.baseWeight = baseWeight; }
}