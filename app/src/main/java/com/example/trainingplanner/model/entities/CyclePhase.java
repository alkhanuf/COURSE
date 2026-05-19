package com.example.trainingplanner.model.entities;

public class CyclePhase {
    private int weeks;
    private double percentage;
    private int reps;

    public CyclePhase(int weeks, double percentage, int reps) {
        this.weeks = weeks;
        this.percentage = percentage;
        this.reps = reps;
    }

    public int getWeeks() { return weeks; }
    public double getPercentage() { return percentage; }
    public int getReps() { return reps; }
}