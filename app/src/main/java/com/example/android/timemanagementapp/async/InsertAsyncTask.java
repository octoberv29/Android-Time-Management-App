package com.example.android.timemanagementapp.async;

import android.os.AsyncTask;

import com.example.android.timemanagementapp.models.Task;
import com.example.android.timemanagementapp.persistence.TaskDao;

public class InsertAsyncTask extends AsyncTask<Task, Void, Void> {

    private TaskDao taskDao;

    public InsertAsyncTask(TaskDao dao) {
        taskDao = dao;
    }

    @Override
    protected Void doInBackground(Task... tasks) {
        taskDao.insertTasks(tasks);
        return null;
    }

}
