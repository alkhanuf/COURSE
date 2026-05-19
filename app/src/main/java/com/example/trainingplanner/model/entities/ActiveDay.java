package com.example.trainingplanner.model.entities;

import java.util.ArrayList;
import java.util.List;

public class ActiveDay {
    private long id;
    private String title;
    private int weekNumber;
    private List<ActiveExercise> exercises = new ArrayList<>();

    public ActiveDay(long id, String title, int weekNumber) {
        this.id = id;
        this.title = title;
        this.weekNumber = weekNumber;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public int getWeekNumber() { return weekNumber; }

    public List<ActiveExercise> getExercises() { return exercises; }
    public void addExercise(ActiveExercise exercise) { this.exercises.add(exercise); }
}