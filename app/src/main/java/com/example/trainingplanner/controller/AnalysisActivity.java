package com.example.trainingplanner.controller;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.trainingplanner.R;
import com.example.trainingplanner.model.AnalysisLogic;
import com.example.trainingplanner.model.AppDatabase;
import com.example.trainingplanner.model.entities.ActiveExercise;
import com.example.trainingplanner.model.entities.ExerciseAnalysis;

import java.util.List;

public class AnalysisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        TextView tvTotalWeight = findViewById(R.id.tvTotalWeight);
        LinearLayout containerProgress = findViewById(R.id.containerProgress);
        LinearLayout containerNormal = findViewById(R.id.containerNormal);
        LinearLayout containerWeightLoss = findViewById(R.id.containerWeightLoss);

        AppDatabase db = new AppDatabase(this);
        List<ActiveExercise> completedExercises = db.getAllExercisesForAnalysis();

        List<ExerciseAnalysis> analyzedData = AnalysisLogic.calculateParametrs(completedExercises);
        double totalWeight = AnalysisLogic.calculateTotalCycleVolume(analyzedData);

        tvTotalWeight.setText(totalWeight + " кг");

        for (ExerciseAnalysis item : analyzedData) {

            TextView tvRow = new TextView(this);
            tvRow.setTextSize(14);
            tvRow.setPadding(8, 12, 8, 12);

            String text = item.name + "\n" +
                    "   ~ Макс. 1ПМ: План " + item.maxPlan1PM + " кг | Факт " + item.maxActual1PM + " кг\n" +
                    "   ~ Тоннаж упражнения: " + item.totalWeight + " кг\n";
            tvRow.setText(text);

            if (item.status == 1) {
                containerProgress.addView(tvRow);
            } else if (item.status == -1) {
                containerWeightLoss.addView(tvRow);
            } else {
                containerNormal.addView(tvRow);
            }
        }

        checkIfEmpty(containerProgress, "Нет упражнений :(");
        checkIfEmpty(containerNormal, "Нет упражнений");
        checkIfEmpty(containerWeightLoss, "Нет упражнений :D");
    }

    private void checkIfEmpty(LinearLayout container, String emptyMessage) {
        if (container.getChildCount() == 0) {
            TextView tvEmpty = new TextView(this);
            tvEmpty.setText(emptyMessage);
            tvEmpty.setTextColor(Color.GRAY);
            tvEmpty.setPadding(8, 12, 8, 12);
            container.addView(tvEmpty);
        }
    }
}