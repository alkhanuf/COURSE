package com.example.trainingplanner.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trainingplanner.R;
import com.example.trainingplanner.model.AppDatabase;
import com.example.trainingplanner.model.entities.CyclePhase;
import com.example.trainingplanner.model.entities.ProgramTemplate;

import java.util.ArrayList;
import java.util.List;

public class NewCycleActivity extends AppCompatActivity {

    private Spinner spinnerPrograms;
    private LinearLayout phasesList;
    private Button btnAddPhase;
    private Button btnGenerateCycle;


    private AppDatabase db;
    private List<ProgramTemplate> programsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cycle);


        db = new AppDatabase(this);

        spinnerPrograms = findViewById(R.id.spinnerPrograms);
        phasesList = findViewById(R.id.phasesList);
        btnAddPhase = findViewById(R.id.btnAddPhase);
        btnGenerateCycle = findViewById(R.id.btnGenerateCycle);

        setupProgramsSpinner();

        addPhaseField();

        btnAddPhase.setOnClickListener(v -> addPhaseField());
        btnGenerateCycle.setOnClickListener(v -> generateCycle());
    }

    private void setupProgramsSpinner() {
        programsList = db.getAllProgramTemplates();

        List<String> programNames = new ArrayList<>();

        for (ProgramTemplate p : programsList) {
            programNames.add(p.getProgramName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, programNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrograms.setAdapter(adapter);
    }

    private void addPhaseField() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View phaseView = inflater.inflate(R.layout.item_phase, phasesList, false);

        ImageButton btnDeletePhase = phaseView.findViewById(R.id.btnDeletePhase);


        btnDeletePhase.setOnClickListener(v -> {
            if (phasesList.getChildCount() > 1) {
                phasesList.removeView(phaseView);
            } else {


                Toast.makeText(this, "Цикл должен содержать хотя бы одну фазу", Toast.LENGTH_SHORT).show();
            }
        });

        phasesList.addView(phaseView);
    }

    private void generateCycle() {
        int selectedProgramIndex = spinnerPrograms.getSelectedItemPosition();
        ProgramTemplate selectedProgram = programsList.get(selectedProgramIndex);

        List<CyclePhase> phasesToGenerate = new ArrayList<>();
        int childCount = phasesList.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View phaseView = phasesList.getChildAt(i);

            EditText etWeeks = phaseView.findViewById(R.id.etWeeks);
            EditText etPercentage = phaseView.findViewById(R.id.etPercentage);
            EditText etReps = phaseView.findViewById(R.id.etReps);

            String weeksStr = etWeeks.getText().toString().trim();
            String percentStr = etPercentage.getText().toString().trim();
            String repsStr = etReps.getText().toString().trim();

            if (weeksStr.isEmpty() || percentStr.isEmpty() || repsStr.isEmpty()) {
                Toast.makeText(this, "Заполните все поля во всех фазах!", Toast.LENGTH_SHORT).show();
                return;
            }


            int weeks = Integer.parseInt(weeksStr);
            double percentage = Double.parseDouble(percentStr);
            int reps = Integer.parseInt(repsStr);
            phasesToGenerate.add(new CyclePhase(weeks, percentage, reps));


        }
        db.clearActiveCycle();
        db.generateNewCycle(selectedProgram, phasesToGenerate);

        finish();
    }
}