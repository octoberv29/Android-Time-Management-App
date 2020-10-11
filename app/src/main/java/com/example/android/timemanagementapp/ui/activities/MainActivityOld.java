package com.example.android.timemanagementapp.ui.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.timemanagementapp.R;
import com.example.android.timemanagementapp.databinding.ActivityMainBinding;
import com.example.android.timemanagementapp.models.Task;
import com.example.android.timemanagementapp.persistence.TaskRepository;
import com.example.android.timemanagementapp.ui.adapters.TaskAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


public class MainActivityOld extends AppCompatActivity implements
        TaskAdapter.OnTaskClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;

    private ActionBarDrawerToggle drawerToggle; //triple horizontal bar
    private TaskAdapter mAdapter;

    private List<Task> mTasks = new ArrayList<>();
    private TaskRepository mTaskRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.navigationView.setNavigationItemSelectedListener(this);
        binding.navigationView.setCheckedItem(R.id.menu_inbox);

        setSupportActionBar(binding.mainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Inbox");

        drawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close);
        binding.drawerLayout.addDrawerListener(drawerToggle);

        mAdapter = new TaskAdapter(MainActivityOld.this, mTasks, this);
        binding.recyclerTasks.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerTasks.setHasFixedSize(true);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Task taskToDelete = mTasks.get(viewHolder.getAdapterPosition());

                mTasks.remove(taskToDelete);
                mAdapter.notifyDataSetChanged();
                mTaskRepository.deleteTask(taskToDelete);
            }
        }).attachToRecyclerView(binding.recyclerTasks);
        binding.recyclerTasks.setAdapter(mAdapter);

        binding.addTaskBtn.setColorFilter(Color.WHITE);
        binding.addTaskBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivityOld.this, TaskActivityOld.class);
            startActivity(intent);
        });

        mTaskRepository = new TaskRepository(this);
        retrieveTasks();
    }

    private void retrieveTasks() {
        mTaskRepository.retrieveTasks().observe(this, tasks -> {
            mTasks.clear();
            if (tasks != null) {
                mTasks.addAll(tasks);
            }
            mAdapter.notifyDataSetChanged();
        });
    }

    // --- Methods for Drawer Layout ---

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    // --- Methods for Navigation ---

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_priority:
                getSupportActionBar().setTitle("Priority");
                binding.drawerLayout.closeDrawers();
                mTaskRepository.retrievePriorityTasks().observe(this, tasks -> {
                    mTasks.clear();
                    if (tasks != null) {
                        mTasks.addAll(tasks);
                    }
                    mAdapter.notifyDataSetChanged();
                });
                return true;
            case R.id.menu_inbox:
                getSupportActionBar().setTitle("Inbox");
                binding.drawerLayout.closeDrawers();
                mTaskRepository.retrieveTasks().observe(this, tasks -> {
                    mTasks.clear();
                    if (tasks != null) {
                        mTasks.addAll(tasks);
                    }
                    mAdapter.notifyDataSetChanged();
                });
                return true;
            case R.id.menu_completed:
                getSupportActionBar().setTitle("Completed");
                binding.drawerLayout.closeDrawers();
                mTaskRepository.retrieveCompletedTasks().observe(this, tasks -> {
                    mTasks.clear();
                    if (tasks != null) {
                        mTasks.addAll(tasks);
                    }
                    mAdapter.notifyDataSetChanged();
                });
                return true;
        }
        return false;
    }

    // --- Methods for TaskAdapter.OnTaskClickListener ---
    @Override
    public void onClick(int position) {
        Task task = mTasks.get(position);
        Intent intent = new Intent(MainActivityOld.this, TaskActivityOld.class);
        intent.putExtra("selected_task", task);
        startActivity(intent);
    }
}