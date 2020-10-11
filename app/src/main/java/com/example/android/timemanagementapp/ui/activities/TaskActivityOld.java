package com.example.android.timemanagementapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.android.timemanagementapp.databinding.ActivityEditTaskBinding;
import com.example.android.timemanagementapp.models.Task;
import com.example.android.timemanagementapp.persistence.TaskRepository;

import java.util.Calendar;

public class TaskActivityOld extends AppCompatActivity {

    private ActivityEditTaskBinding binding;
    private TaskRepository repository;
    private Task task;
    private boolean isNewTask = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.editToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        repository = new TaskRepository(this);

        if (getIntent().hasExtra("selected_task")) {
            getExistingTask();
            isNewTask = false;
            if (task.getTitle() != null) {
                getSupportActionBar().setTitle(task.getTitle());
            }
        } else {
            getNewTask();
            isNewTask = true;
            getSupportActionBar().setTitle("New Task");
        }

        binding.saveTaskBtn.setOnClickListener(v -> {
            saveTask();
            finish();
        });
    }

    private void getExistingTask() {
        task = getIntent().getParcelableExtra("selected_task");
        binding.titleEdit.setText(task.getTitle());
        binding.extraContent.description.setText(task.getDescription());
    }

    private void getNewTask() {
        String title = "";
        String description = "";
        long created_at = Calendar.getInstance().getTimeInMillis();
        boolean completed = false;
        boolean priority = false;
        long due_date = Calendar.getInstance().getTimeInMillis();
        boolean reminder = false;
        long remind_at = Calendar.getInstance().getTimeInMillis();
        task = new Task(title, description, created_at, completed, priority, due_date, reminder, remind_at);
    }

    private void saveTask() {
        String title = String.valueOf(binding.titleEdit.getText()).trim();
        String description = String.valueOf(binding.extraContent.description).trim();

        task.setTitle(title);
        task.setDescription(description);

        if (isNewTask) {
            repository.insertTask(task);
        } else {
            repository.updateTask(task);
        }
    }
}