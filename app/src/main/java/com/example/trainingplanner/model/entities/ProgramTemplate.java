package com.example.trainingplanner.model.entities;

import java.util.ArrayList;
import java.util.List;

public class ProgramTemplate {
    private long id;
    private String programName;
    private List<DayTemplate> days;

    public ProgramTemplate(String programName) {
        this.programName = programName;
        this.days = new ArrayList<>();
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getProgramName() { return programName; }
    public void setProgramName(String programName) { this.programName = programName; }

    public List<DayTemplate> getDays() { return days; }
    public void addDay(DayTemplate day) { this.days.add(day); }
}