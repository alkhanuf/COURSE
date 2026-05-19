package com.example.trainingplanner.model.entities;

public class ActiveExercise {
    private String name;
    private double planWeight;
    private int planReps;

    private double actualWeight;
    private int actualReps;

    public ActiveExercise(String name, double planWeight, int planReps) {
        this.name = name;
        this.planWeight = planWeight;
        this.planReps = planReps;
        this.actualWeight = 0;
        this.actualReps = 0;
    }

    public String getName() { return name; }
    public double getPlanWeight() { return planWeight; }
    public int getPlanReps() { return planReps; }

    public double getActualWeight() { return actualWeight; }
    public void setActualWeight(double actualWeight) { this.actualWeight = actualWeight; }

    public int getActualReps() { return actualReps; }
    public void setActualReps(int actualReps) { this.actualReps = actualReps; }
}