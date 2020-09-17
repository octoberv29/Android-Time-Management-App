package com.example.android.timemanagementapp.async;

import android.os.AsyncTask;

import com.example.android.timemanagementapp.models.Task;
import com.example.android.timemanagementapp.persistence.TaskDao;

public class DeleteAsyncTask extends AsyncTask<Task, Void, Void> {

    private TaskDao taskDao;

    public DeleteAsyncTask(TaskDao dao) {
        taskDao = dao;
    }

    @Override
    protected Void doInBackground(Task... tasks) {
        taskDao.delete(tasks);
        return null;
    }

}