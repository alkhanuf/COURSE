package com.example.trainingplanner.controller;

import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.trainingplanner.R;
import com.example.trainingplanner.model.AppDatabase;
import com.example.trainingplanner.model.entities.Exercise;
import java.util.ArrayList;
import java.util.List;

public class ExercisesActivity extends AppCompatActivity {

    private AppDatabase db;
    private ListView lvExercises;
    private ArrayAdapter<String> adapter;
    private List<Exercise> exerciseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        db = new AppDatabase(this);
        lvExercises = findViewById(R.id.lvExercises);
        Button btnAddExercise = findViewById(R.id.btnAddExercise);

        refreshList();

        btnAddExercise.setOnClickListener(v -> showAddExerciseDialog());

        lvExercises.setOnItemClickListener((parent, view, position, id) -> {
            Exercise selectedExercise = exerciseList.get(position);
            showActionDialog(selectedExercise);
        });
    }

    private void refreshList() {
        exerciseList = db.getAllExercises();

        ArrayList<String> displayList = new ArrayList<>();
        for (Exercise ex : exerciseList) {
            displayList.add(ex.getName() + " — " + ex.getBaseWeight() + " кг");
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        lvExercises.setAdapter(adapter);
    }

    private void showAddExerciseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, com.google.android.material.R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);
        builder.setTitle("Новое упражнение");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        EditText inputName = new EditText(this);
        inputName.setHint("Название");
        layout.addView(inputName);

        EditText inputWeight = new EditText(this);
        inputWeight.setHint("Базовый вес (кг * 8-12 повторений)");
        inputWeight.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(inputWeight);

        builder.setView(layout);

        builder.setPositiveButton("Добавить", (dialog, which) -> {
            String name = inputName.getText().toString();
            String weightStr = inputWeight.getText().toString();

            if (!name.isEmpty() && !weightStr.isEmpty()) {
                double weight = Double.parseDouble(weightStr);
                db.addExercise(new Exercise(name, weight));
                refreshList();
            }
        });

        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void showActionDialog(Exercise exercise) {
        String[] options = {"Редактировать", "Удалить"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this, com.google.android.material.R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);
        builder.setTitle(exercise.getName());
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                showEditExerciseDialog(exercise);
            } else if (which == 1) {
                db.deleteExercise(exercise.getId());
                refreshList();
            }
        });
        builder.show();
    }
    private void showEditExerciseDialog(Exercise exercise) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, com.google.android.material.R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);
        builder.setTitle("Редактирование");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        EditText inputName = new EditText(this);
        inputName.setHint("Название");
        inputName.setText(exercise.getName());
        layout.addView(inputName);

        EditText inputWeight = new EditText(this);
        inputWeight.setHint("Базовый вес");
        inputWeight.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        inputWeight.setText(String.valueOf(exercise.getBaseWeight()));
        layout.addView(inputWeight);

        builder.setView(layout);

        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            String newName = inputName.getText().toString();
            String weightStr = inputWeight.getText().toString();

            if (!newName.isEmpty() && !weightStr.isEmpty()) {
                exercise.setName(newName);
                exercise.setBaseWeight(Double.parseDouble(weightStr));

                db.updateExercise(exercise);
                refreshList();
            }
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }
}