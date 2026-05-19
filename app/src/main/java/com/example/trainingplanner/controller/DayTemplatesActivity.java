package com.example.trainingplanner.controller;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.trainingplanner.R;
import com.example.trainingplanner.model.AppDatabase;
import com.example.trainingplanner.model.entities.DayTemplate;
import com.example.trainingplanner.model.entities.Exercise;
import java.util.ArrayList;
import java.util.List;

public class DayTemplatesActivity extends AppCompatActivity {

    private AppDatabase db;
    private ListView lvDayTemplates;
    private List<DayTemplate> dayTemplates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_templates);

        db = new AppDatabase(this);
        lvDayTemplates = findViewById(R.id.lvDayTemplates);
        Button btnAddDayTemplate = findViewById(R.id.btnAddDayTemplate);

        refreshList();

        btnAddDayTemplate.setOnClickListener(v -> showCreateDayDialog());

        lvDayTemplates.setOnItemClickListener((parent, view, position, id) -> {
            showActionDialog(dayTemplates.get(position));
        });
    }

    private void refreshList() {
        dayTemplates = db.getAllDayTemplatesWithExercises();

        DayTemplateAdapter adapter = new DayTemplateAdapter(this, dayTemplates);
        lvDayTemplates.setAdapter(adapter);
    }

    private void showCreateDayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, com.google.android.material.R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);
        builder.setTitle("Название дня");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Далее", (dialog, which) -> {
            String title = input.getText().toString();
            if (!title.isEmpty()) {
                showSelectExercisesDialog(title);
            }
        });
        builder.show();
    }

    private void showSelectExercisesDialog(String title) {
        List<Exercise> allExercises = db.getAllExercises();
        if (allExercises.isEmpty()) {
            Toast.makeText(this, "Сначала создайте упражнения!", Toast.LENGTH_LONG).show();
            return;
        }

        String[] names = new String[allExercises.size()];
        boolean[] checkedItems = new boolean[allExercises.size()];
        for (int i = 0; i < allExercises.size(); i++) {
            names[i] = allExercises.get(i).getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, com.google.android.material.R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);
        builder.setTitle("Выберите упражнения для дня " + title);

        builder.setMultiChoiceItems(names, checkedItems, (dialog, which, isChecked) -> {
            checkedItems[which] = isChecked;
        });

        builder.setPositiveButton("Готово", (dialog, which) -> {
            long dayId = db.addDayTemplate(title);

            for (int i = 0; i < checkedItems.length; i++) {
                if (checkedItems[i]) {
                    db.addExerciseToDay(dayId, allExercises.get(i).getId());
                }
            }
            refreshList();
        });

        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void showActionDialog(DayTemplate day) {
        String[] options = {"Редактировать", "Удалить"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this, com.google.android.material.R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);
        builder.setTitle(day.getTitle());
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                showEditDayDialog(day);
            } else if (which == 1) {
                db.deleteDayTemplate(day.getId());
                refreshList();
            }
        });
        builder.show();
    }

    private void showEditDayDialog(DayTemplate day) {
        List<Exercise> allExercises = db.getAllExercises();
        List<Long> currentIds = db.getExerciseIdsForDay(day.getId());

        String[] names = new String[allExercises.size()];
        boolean[] checkedItems = new boolean[allExercises.size()];

        for (int i = 0; i < allExercises.size(); i++) {
            names[i] = allExercises.get(i).getName();
            if (currentIds.contains(allExercises.get(i).getId())) {
                checkedItems[i] = true;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, com.google.android.material.R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);
        builder.setTitle("Редактировать: " + day.getTitle());
        builder.setMultiChoiceItems(names, checkedItems, (dialog, which, isChecked) -> {
            checkedItems[which] = isChecked;
        });

        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            List<Long> selectedIds = new ArrayList<>();
            for (int i = 0; i < checkedItems.length; i++) {
                if (checkedItems[i]) selectedIds.add(allExercises.get(i).getId());
            }
            db.updateDayExercises(day.getId(), selectedIds);
            refreshList();
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }
}