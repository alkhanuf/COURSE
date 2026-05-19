package com.example.trainingplanner.model.entities;

import java.util.ArrayList;
import java.util.List;

public class DayTemplate {
    private long id;
    private String title;
    private List<Exercise> exercises;

    public DayTemplate(String title) {
        this.title = title;
        this.exercises = new ArrayList<>();
    }

    public DayTemplate(long id, String title) {
        this.id = id;
        this.title = title;
        this.exercises = new ArrayList<>();
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<Exercise> getExercises() { return exercises; }
    public void addExercise(Exercise exercise) { this.exercises.add(exercise); }
}