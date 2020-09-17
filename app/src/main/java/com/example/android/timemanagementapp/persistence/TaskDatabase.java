package com.example.android.timemanagementapp.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.android.timemanagementapp.models.Task;

@Database(entities = {Task.class}, version = 1)
public abstract class TaskDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "tasks_db";

    private static TaskDatabase instance;

    static TaskDatabase getInstance(final Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), TaskDatabase.class, DATABASE_NAME).build();
        }
        return instance;
    }

    public abstract TaskDao getNoteDao();

}
