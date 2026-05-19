package com.example.trainingplanner.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.trainingplanner.R;
import com.example.trainingplanner.model.entities.DayTemplate;
import com.example.trainingplanner.model.entities.ProgramTemplate;

import java.util.List;

public class ProgramTemplateAdapter extends ArrayAdapter<ProgramTemplate> {

    public ProgramTemplateAdapter(Context context, List<ProgramTemplate> programs) {
        super(context, 0, programs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProgramTemplate program = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_day_template, parent, false);
        }

        TextView tvTitle = convertView.findViewById(R.id.tvDayTitle);
        TextView tvDays = convertView.findViewById(R.id.tvDayExercises);

        tvTitle.setText(program.getProgramName());

        StringBuilder sb = new StringBuilder();
        List<DayTemplate> days = program.getDays();
        for (int i = 0; i < days.size(); i++) {
            sb.append(days.get(i).getTitle());
            if (i < days.size() - 1) sb.append(" -> ");
        }

        if (days.isEmpty()) {
            tvDays.setText("Нет тренировочных дней");
        } else {
            tvDays.setText(sb.toString());
        }

        return convertView;
    }
}