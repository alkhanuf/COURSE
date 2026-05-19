package com.example.trainingplanner.controller;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trainingplanner.R;
import com.example.trainingplanner.model.AppDatabase;
import com.example.trainingplanner.model.entities.DayTemplate;
import com.example.trainingplanner.model.entities.ProgramTemplate;

import java.util.ArrayList;
import java.util.List;

public class ProgramTemplatesActivity extends AppCompatActivity {

    private AppDatabase db;
    private ListView lvProgramTemplates;
    private List<ProgramTemplate> programTemplates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_templates);

        db = new AppDatabase(this);
        lvProgramTemplates = findViewById(R.id.lvProgramTemplates);
        Button btnAddProgramTemplate = findViewById(R.id.btnAddProgramTemplate);

        refreshList();

        btnAddProgramTemplate.setOnClickListener(v -> showCreateProgramDialog());

        lvProgramTemplates.setOnItemClickListener((parent, view, position, id) -> {
            showActionDialog(programTemplates.get(position));
        });
    }

    private void refreshList() {
        programTemplates = db.getAllProgramTemplates();
        ProgramTemplateAdapter adapter = new ProgramTemplateAdapter(this, programTemplates);
        lvProgramTemplates.setAdapter(adapter);
    }

    private void showCreateProgramDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, com.google.android.material.R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);
        builder.setTitle("Название программы");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Далее", (dialog, which) -> {
            String title = input.getText().toString().trim();
            if (!title.isEmpty()) {
                showSelectDaysDialog(title);
            }
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void showSelectDaysDialog(String programTitle) {
        List<DayTemplate> allDays = db.getAllDayTemplatesWithExercises();
        if (allDays.isEmpty()) {
            Toast.makeText(this, "Сначала создайте шаблоны дней!", Toast.LENGTH_LONG).show();
            return;
        }

        String[] names = new String[allDays.size()];
        boolean[] checkedItems = new boolean[allDays.size()];
        for (int i = 0; i < allDays.size(); i++) {
            names[i] = allDays.get(i).getTitle();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, com.google.android.material.R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);
        builder.setTitle("Выберите дни для программы " + programTitle);

        builder.setMultiChoiceItems(names, checkedItems, (dialog, which, isChecked) -> {
            checkedItems[which] = isChecked;
        });

        builder.setPositiveButton("Готово", (dialog, which) -> {
            long programId = db.addProgramTemplate(programTitle);

            int order = 1;
            for (int i = 0; i < checkedItems.length; i++) {
                if (checkedItems[i]) {
                    db.addDayToProgram(programId, allDays.get(i).getId(), order);
                    order++;
                }
            }
            refreshList();
        });

        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void showActionDialog(ProgramTemplate program) {
        String[] options = {"Удалить"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this, com.google.android.material.R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);
        builder.setTitle(program.getProgramName());
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                db.deleteProgramTemplate(program.getId());
                refreshList();
            }
        });
        builder.show();
    }
}