package com.example.trainingplanner.controller;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trainingplanner.MainActivity;
import com.example.trainingplanner.R;
import com.example.trainingplanner.model.AppDatabase;
import com.example.trainingplanner.model.entities.ActiveExercise;

import java.util.List;

public class WorkoutActivity extends AppCompatActivity {
    private AppDatabase db;
    private long dayId;
    private List<ActiveExercise> exercisesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        db = new AppDatabase(this);

        TextView tvWorkoutTitle = findViewById(R.id.tvWorkoutTitle);
        LinearLayout container = findViewById(R.id.containerWorkoutExercises);
        Button btnSave = findViewById(R.id.btnSaveWorkout);

        dayId = getIntent().getLongExtra("dayId", -1);
        String dayTitle = getIntent().getStringExtra("dayTitle");
        int weekNum = getIntent().getIntExtra("weekNumber", 1);

        tvWorkoutTitle.setText(dayTitle + " (Неделя " + weekNum + ")");

        exercisesList = db.getExercisesForActiveDay(dayId);
        LayoutInflater inflater = LayoutInflater.from(this);

        for (ActiveExercise ex : exercisesList) {
            View exView = inflater.inflate(R.layout.item_workout_exercise, container, false);

            TextView tvName = exView.findViewById(R.id.tvExName);
            TextView tvPlan = exView.findViewById(R.id.tvExPlanInfo);
            EditText etWeight = exView.findViewById(R.id.etActualWeight);
            EditText etReps = exView.findViewById(R.id.etActualReps);

            tvName.setText(ex.getName());
            if (ex.getPlanWeight() == 0) {
                tvPlan.setText("План: " + ex.getPlanReps() + " повт.");
            } else {
                tvPlan.setText("План: " + ex.getPlanWeight() + " кг х " + ex.getPlanReps());
            }

            etWeight.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        ex.setActualWeight(Double.parseDouble(s.toString()));
                    } catch (NumberFormatException e) {
                        ex.setActualWeight(0);
                    }
                }
            });

            etReps.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        ex.setActualReps(Integer.parseInt(s.toString()));
                    } catch (NumberFormatException e) {
                        ex.setActualReps(0);
                    }
                }
            });

            container.addView(exView);
        }

        btnSave.setOnClickListener(v -> {
            db.saveWorkoutResults(dayId, exercisesList);

            Toast.makeText(this, "Тренировка сохранена", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(WorkoutActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}