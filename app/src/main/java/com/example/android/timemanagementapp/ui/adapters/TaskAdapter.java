package com.example.android.timemanagementapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.timemanagementapp.R;
import com.example.android.timemanagementapp.models.Task;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private List<Task> tasksList = new ArrayList<>();
    private OnTaskClickListener onTaskClickListener;

    public TaskAdapter(List<Task> tasks, OnTaskClickListener onTaskClickListener) {
        this.tasksList = tasks;
        this.onTaskClickListener = onTaskClickListener;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task task = tasksList.get(position);
        holder.title.setText(task.getTitle());
        holder.timestamp.setText(task.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return tasksList != null ? tasksList.size() : 0;
    }

    class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView timestamp;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.task_title);
            timestamp = itemView.findViewById(R.id.task_timestamp);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onTaskClickListener.onClick(getAdapterPosition());
        }
    }

    public interface OnTaskClickListener {
        public void onClick(int position);
    }
}
