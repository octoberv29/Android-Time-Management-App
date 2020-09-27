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
    void insertTask(Task task);

    @Query("SELECT * FROM tasks_table WHERE id = :id")
    Task getTaskById(long id);

    @Query("SELECT * FROM tasks_table WHERE completed = 0")
    LiveData<List<Task>> getAllUncompletedTasks();

    @Query("SELECT * FROM tasks_table WHERE completed = 1")
    LiveData<List<Task>> getCompletedTasks();

    @Query("SELECT * FROM tasks_table WHERE priority = 1 AND completed = 0")
    LiveData<List<Task>> getPriorityTasks();

    @Delete
    void deleteTask(Task... tasks);

    @Update
    void updateTask(Task... tasks);

}
