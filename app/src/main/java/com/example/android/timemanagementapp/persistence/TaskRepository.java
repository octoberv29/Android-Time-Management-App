package com.example.android.timemanagementapp.persistence;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.android.timemanagementapp.models.Task;

import java.util.List;

public class TaskRepository {
    private TaskDatabase taskDatabase;
    private TaskDao taskDao;

    public TaskRepository(Context context) {
        taskDatabase = TaskDatabase.getInstance(context);
        taskDao = taskDatabase.getNoteDao();
    }

    public void insertTask(Task task) {
        new Thread(() -> taskDao.insertTask(task)).start();
    }

    public Task getTaskById(long id) {
//        Thread(() -> taskDao.getTaskById(id)).start();
        return new Task();
    }

    public LiveData<List<Task>> retrieveTasks() {
        return taskDao.getAllUncompletedTasks();
    }

    public LiveData<List<Task>> retrieveCompletedTasks() {
        return taskDao.getCompletedTasks();
    }

    public LiveData<List<Task>> retrievePriorityTasks() {
        return taskDao.getPriorityTasks();
    }

    public void updateTask(Task task) {
        new Thread(() -> taskDao.updateTask(task)).start();
    }

    public void deleteTask(Task task) {
        new Thread(() -> taskDao.deleteTask(task)).start();
    }
}
