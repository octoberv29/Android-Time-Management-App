package com.example.android.timemanagementapp.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.timemanagementapp.models.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    long[] insertTasks(Task... tasks);

    @Query("SELECT * FROM tasks_table")
    LiveData<List<Task>> getTasks();

    @Delete
    int delete(Task... tasks);

    @Update
    int update(Task... tasks);

}
