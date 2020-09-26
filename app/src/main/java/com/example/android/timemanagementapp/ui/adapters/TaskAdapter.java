package com.example.android.timemanagementapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.timemanagementapp.R;
import com.example.android.timemanagementapp.models.Task;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private List<Task> mTasks = new ArrayList<>();
    private OnTaskClickListener mOnTaskClickListener;

    public TaskAdapter(List<Task> tasks, OnTaskClickListener onTaskClickListener) {
        this.mTasks = tasks;
        this.mOnTaskClickListener = onTaskClickListener;
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
            item.setRadius(16);
            item.setCardElevation(8);

            title = itemView.findViewById(R.id.textview_task_title);

            checkBox = itemView.findViewById(R.id.checkbox_task);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: update the completed of a task
                    if (((CheckBox) view).isChecked()) {
                        String message = "         Great Job!\nTask is now completed";
                        Toast.makeText(view.getContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
            });

            priority = itemView.findViewById(R.id.toggle_priority);
            priority.setOnFavoriteChangeListener((buttonView, favorite) -> {
                //TODO: change priority
            });

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
