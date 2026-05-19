package com.example.trainingplanner.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.trainingplanner.R;
import com.example.trainingplanner.model.entities.DayTemplate;
import com.example.trainingplanner.model.entities.Exercise;
import java.util.List;

public class DayTemplateAdapter extends ArrayAdapter<DayTemplate> {

    public DayTemplateAdapter(Context context, List<DayTemplate> templates) {
        super(context, 0, templates);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DayTemplate day = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_day_template, parent, false);
        }

        TextView tvTitle = convertView.findViewById(R.id.tvDayTitle);
        TextView tvExercises = convertView.findViewById(R.id.tvDayExercises);

        tvTitle.setText(day.getTitle());

        StringBuilder sb = new StringBuilder();
        List<Exercise> exercises = day.getExercises();
        for (int i = 0; i < exercises.size(); i++) {
            sb.append(exercises.get(i).getName());
            if (i < exercises.size() - 1) sb.append(", ");
        }

        if (exercises.isEmpty()) {
            tvExercises.setText("Нет упражнений");
        } else {
            tvExercises.setText(sb.toString());
        }

        return convertView;
    }
}