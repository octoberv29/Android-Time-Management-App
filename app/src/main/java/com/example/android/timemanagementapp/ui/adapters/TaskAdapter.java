package com.example.android.timemanagementapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.timemanagementapp.R;
import com.example.android.timemanagementapp.models.Task;
import com.example.android.timemanagementapp.persistence.TaskRepository;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private List<Task> mTasks = new ArrayList<>();
    private OnTaskClickListener mOnTaskClickListener;

    private TaskRepository repository;

    public TaskAdapter(Context context, List<Task> tasks, OnTaskClickListener onTaskClickListener) {
        this.mTasks = tasks;
        this.mOnTaskClickListener = onTaskClickListener;
        repository = new TaskRepository(context);
    }

    @Override
    public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskHolder holder, int position) {
        Task task = mTasks.get(position);
        holder.title.setText(task.getTitle());
        holder.checkBox.setChecked(task.getCompleted());
        holder.priority.setFavorite(task.getPriority());

        holder.checkBox.setOnClickListener(view -> {
            if (((CheckBox) view).isChecked()) {
                String message = "         Great Job!\nTask is now completed";
                Toast.makeText(view.getContext(), message, Toast.LENGTH_LONG).show();
                task.setCompleted(true);
            } else {
                task.setCompleted(false);
            }
            repository.updateTask(task);
        });

        holder.priority.setOnFavoriteChangeListener((buttonView, favorite) -> {
            if (buttonView.isFavorite()) {
                task.setPriority(true);
            } else {
                task.setPriority(false);
            }
            repository.updateTask(task);
        });
    }

    @Override
    public int getItemCount() {
        return mTasks != null ? mTasks.size() : 0;
    }


    public class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView item;
        private TextView title;
        private CheckBox checkBox;
        private MaterialFavoriteButton priority;

        public TaskHolder(View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.cardview_item);
            title = itemView.findViewById(R.id.textview_task_title);
            checkBox = itemView.findViewById(R.id.checkbox_task);
            priority = itemView.findViewById(R.id.toggle_priority);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnTaskClickListener.onClick(getAdapterPosition());
        }
    }

    public interface OnTaskClickListener {
        public void onClick(int position);
    }
}
