package com.example.trainingplanner.model;

import com.example.trainingplanner.model.entities.ActiveExercise;
import com.example.trainingplanner.model.entities.ExerciseAnalysis;

import java.util.ArrayList;
import java.util.List;

public class AnalysisLogic {

    public static List<ExerciseAnalysis> calculateParametrs(List<ActiveExercise> completedExercises) {
        List<ExerciseAnalysis> results = new ArrayList<>();

        for (ActiveExercise ex : completedExercises) {
            String name = ex.getName();

            double plan1PM = ex.getPlanWeight() * (1 + (double) ex.getPlanReps() / 30);
            double act1PM = ex.getActualWeight() * (1 + (double) ex.getActualReps() / 30);
            double currentVolume = ex.getActualWeight() * ex.getActualReps();

            ExerciseAnalysis existingItem = null;
            for (ExerciseAnalysis item : results) {
                if (item.name.equals(name)) {
                    existingItem = item;
                    break;
                }
            }

            if (existingItem != null) {
                if (plan1PM > existingItem.maxPlan1PM) {
                    existingItem.maxPlan1PM = plan1PM;
                }
                if (act1PM > existingItem.maxActual1PM) {
                    existingItem.maxActual1PM = act1PM;
                }
                existingItem.totalWeight += currentVolume;
            } else {

                ExerciseAnalysis newItem = new ExerciseAnalysis();

                newItem.name = name;
                newItem.maxPlan1PM = plan1PM;
                newItem.maxActual1PM = act1PM;
                newItem.totalWeight = currentVolume;
                results.add(newItem);
            }
        }

        for (ExerciseAnalysis item : results) {
            item.maxPlan1PM = Math.round(item.maxPlan1PM);
            item.maxActual1PM = Math.round(item.maxActual1PM);

            if (item.maxActual1PM > item.maxPlan1PM) {
                item.status = 1;
            } else if (item.maxActual1PM < item.maxPlan1PM) {
                item.status = -1;
            } else {
                item.status = 0;
            }
        }

        return results;
    }

    public static double calculateTotalCycleVolume(List<ExerciseAnalysis> analyzedData) {
        double total = 0;
        for (ExerciseAnalysis item : analyzedData) {
            total += item.totalWeight;
        }
        return total;
    }
}