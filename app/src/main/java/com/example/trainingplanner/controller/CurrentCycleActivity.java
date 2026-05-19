package com.example.trainingplanner.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trainingplanner.MainActivity;
import com.example.trainingplanner.R;
import com.example.trainingplanner.model.AppDatabase;
import com.example.trainingplanner.model.entities.ActiveDay;

import java.util.ArrayList;
import java.util.List;

public class CurrentCycleActivity extends AppCompatActivity {

    private TextView tvProgramTitle;
    private Spinner spinnerWeeks;
    private RecyclerView rvActiveDays;

    private AppDatabase db;
    private ActiveDaysAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_cycle);

        db = new AppDatabase(this);

        tvProgramTitle = findViewById(R.id.tvProgramTitle);
        spinnerWeeks = findViewById(R.id.spinnerWeeks);
        rvActiveDays = findViewById(R.id.rvActiveDays);

        rvActiveDays.setLayoutManager(new LinearLayoutManager(this));

        String programTitle = db.getActiveCycleProgramTitle();
        if (programTitle == null) {
            Toast.makeText(this, "У вас нет активного цикла", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        tvProgramTitle.setText("Цикл: " + programTitle);

        setupWeeksSpinner();
    }

    private void setupWeeksSpinner() {
        int totalWeeks = db.getMaxWeeksInActiveCycle();

        List<String> weekLabels = new ArrayList<>();
        for (int i = 1; i <= totalWeeks; i++) {

            weekLabels.add("Неделя " + i);
        }



        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, weekLabels);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeeks.setAdapter(spinnerAdapter);

        adapter = new ActiveDaysAdapter(new ArrayList<>(), day -> {
            Intent intent = new Intent(CurrentCycleActivity.this, WorkoutActivity.class);
            intent.putExtra("dayId", day.getId());
            intent.putExtra("dayTitle", day.getTitle());
            intent.putExtra("weekNumber", day.getWeekNumber());
            startActivity(intent);
        });
        rvActiveDays.setAdapter(adapter);

        spinnerWeeks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<ActiveDay> daysForWeek = db.getActiveDaysWithExercisesForWeek(position + 1);

                adapter.updateData(daysForWeek);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}