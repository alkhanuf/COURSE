package com.example.trainingplanner.controller;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trainingplanner.R;
import com.example.trainingplanner.model.entities.ActiveDay;
import com.example.trainingplanner.model.entities.ActiveExercise;

import java.util.List;

public class ActiveDaysAdapter extends RecyclerView.Adapter<ActiveDaysAdapter.DayViewHolder> {

    private List<ActiveDay> days;
    public interface OnDayClickListener {
        void onDayClick(ActiveDay day);
    }
    private OnDayClickListener clickListener;
    public ActiveDaysAdapter(List<ActiveDay> days, OnDayClickListener clickListener) {
        this.days = days;
        this.clickListener = clickListener;
    }

    public void updateData(List<ActiveDay> newDays) {
        this.days = newDays;
        notifyDataSetChanged();
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_active_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        ActiveDay currentDay = days.get(position);
        holder.tvDayTitle.setText(currentDay.getTitle());

        if (currentDay.isCompleted()) {
            holder.itemView.setAlpha(0.4f);
            holder.itemView.setOnClickListener(v -> {
                Toast.makeText(holder.itemView.getContext(), "Тренировка выполнена", Toast.LENGTH_LONG).show();
            });
        } else {
            holder.itemView.setAlpha(1f);
            holder.itemView.setOnClickListener(v -> {clickListener.onDayClick(currentDay);
            });
        }

        holder.containerExercises.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(holder.itemView.getContext());

        for (ActiveExercise ex : currentDay.getExercises()) {
            View exView = inflater.inflate(R.layout.item_active_exercise, holder.containerExercises, false);

            TextView tvName = exView.findViewById(R.id.tvExerciseName);
            TextView tvPlan = exView.findViewById(R.id.tvExercisePlan);

            tvName.setText(ex.getName());

            if (ex.getPlanWeight() == 0) {
                tvPlan.setText(ex.getPlanReps() + " повт.");
            } else {
                String weightStr = String.valueOf(ex.getPlanWeight());
                tvPlan.setText(weightStr + " кг х " + ex.getPlanReps());
            }

            holder.containerExercises.addView(exView);
        }
    }

    @Override
    public int getItemCount() {
        if (days != null) {
            return days.size();
        }
        else {
            return 0;
        }
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayTitle;
        LinearLayout containerExercises;

        public DayViewHolder(View itemView) {
            super(itemView);
            tvDayTitle = itemView.findViewById(R.id.tvDayTitle);
            containerExercises = itemView.findViewById(R.id.containerActiveExercises);
        }
    }
}