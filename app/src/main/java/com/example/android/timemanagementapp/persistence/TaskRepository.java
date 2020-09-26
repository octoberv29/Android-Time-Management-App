package com.example.android.timemanagementapp.persistence;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.android.timemanagementapp.async.DeleteAsyncTask;
import com.example.android.timemanagementapp.async.InsertAsyncTask;
import com.example.android.timemanagementapp.async.UpdateAsyncTask;
import com.example.android.timemanagementapp.models.Task;

import java.util.List;

public class TaskRepository {
    private TaskDatabase taskDatabase;

    public TaskRepository(Context context) {
        taskDatabase = TaskDatabase.getInstance(context);
    }

    public void insertTask(Task task) {
        new InsertAsyncTask(taskDatabase.getNoteDao()).execute(task);
    }

    public void updateTask(Task task) {
        new UpdateAsyncTask(taskDatabase.getNoteDao()).execute(task);
    }

    public LiveData<List<Task>> retrieveTasks() {
        return taskDatabase.getNoteDao().getAllTasks();
    }

    public void deleteTask(Task task) {
        new DeleteAsyncTask(taskDatabase.getNoteDao()).execute(task);
    }
}
