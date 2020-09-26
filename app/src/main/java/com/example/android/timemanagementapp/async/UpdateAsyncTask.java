package com.example.android.timemanagementapp.async;

import android.os.AsyncTask;

import com.example.android.timemanagementapp.models.Task;
import com.example.android.timemanagementapp.persistence.TaskDao;

public class UpdateAsyncTask extends AsyncTask<Task, Void, Void> {

    private TaskDao mTaskDao;

    public UpdateAsyncTask(TaskDao dao) {
        mTaskDao = dao;
    }

    @Override
    protected Void doInBackground(Task... tasks) {
        mTaskDao.updateTask(tasks[0]);
        return null;
    }

}
