package com.example.trainingplanner;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;

import com.example.trainingplanner.controller.CurrentCycleActivity;
import com.example.trainingplanner.controller.ExercisesActivity;
import com.example.trainingplanner.controller.DayTemplatesActivity;
import com.example.trainingplanner.controller.ProgramTemplatesActivity;
import com.example.trainingplanner.controller.NewCycleActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnCurrentCycle = findViewById(R.id.btnCurrentCycle);
        Button btnExercises = findViewById(R.id.btnExercises);
        Button btnTemplates = findViewById(R.id.btnTemplates);
        Button btnNewCycle = findViewById(R.id.btnNewCycle);
        Button btnTemplatesProgram = findViewById(R.id.btnTemplatesProgram);

        btnCurrentCycle.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CurrentCycleActivity.class);
            startActivity(intent);
        });
        btnExercises.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ExercisesActivity.class);
            startActivity(intent);
        });

        btnTemplates.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DayTemplatesActivity.class);
            startActivity(intent);
        });

        btnNewCycle.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewCycleActivity.class);
            startActivity(intent);
        });

        btnTemplatesProgram.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ProgramTemplatesActivity.class);
            startActivity(intent);
        });
    }
}