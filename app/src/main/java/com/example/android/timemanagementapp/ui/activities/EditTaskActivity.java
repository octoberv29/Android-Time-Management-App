package com.example.android.timemanagementapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.android.timemanagementapp.databinding.ActivityEditTaskBinding;
import com.example.android.timemanagementapp.models.Task;
import com.example.android.timemanagementapp.persistence.TaskRepository;

import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {

    private ActivityEditTaskBinding binding;
    private TaskRepository repository;
    private Task task;
    private boolean isNewTask = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new TaskRepository(this);

        if (getIntent().hasExtra("selected_task")) {
            getExistingTask();
            isNewTask = false;
        } else {
            getNewTask();
            isNewTask = true;
        }

        setSupportActionBar(binding.editToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.saveTask.setOnClickListener(v -> {
            saveTask();
            finish();
        });
    }

    private void getExistingTask() {
        task = getIntent().getParcelableExtra("selected_task");
        binding.title.setText(task.getTitle());
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
        String title = String.valueOf(binding.title.getText()).trim();
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