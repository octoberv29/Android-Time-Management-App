package com.example.android.timemanagementapp.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.android.timemanagementapp.R;
import com.example.android.timemanagementapp.models.Task;
import com.example.android.timemanagementapp.persistence.TaskRepository;
import com.example.android.timemanagementapp.ui.adapters.TaskAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        TaskAdapter.OnTaskClickListener,
        FloatingActionButton.OnClickListener
{

    private static final String TAG = "NotesListActivity";

    // ui components
    private RecyclerView mRecyclerView;

    // vars
    private ArrayList<Task> mTasks = new ArrayList<>();
    private TaskAdapter mTaskAdapter;
    private TaskRepository mTaskRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.tasks_RecyclerView);

        findViewById(R.id.fab).setOnClickListener(this);

        initRecyclerView();
        mTaskRepository = new TaskRepository(this);
        retrieveNotes();
//        insertFakeNotes();

        setSupportActionBar((Toolbar)findViewById(R.id.main_toolbar));
        setTitle("Notes");
    }


    private void retrieveNotes() {
        mTaskRepository.retrieveTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {
                if(mTasks.size() > 0){
                    mTasks.clear();
                }
                if(tasks != null){
                    mTasks.addAll(tasks);
                }
                mTaskAdapter.notifyDataSetChanged();
            }
        });
    }

    private void insertFakeNotes(){
        for(int i = 0; i < 1000; i++){
            Task task = new Task();
            task.setTitle("title #" + i);
            task.setContent("content #: " + i);
            task.setTimestamp("Jan 2019");
            mTasks.add(task);
        }
        mTaskAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
//        mRecyclerView.addItemDecoration(itemDecorator);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mTaskAdapter = new TaskAdapter(mTasks, this);
        mRecyclerView.setAdapter(mTaskAdapter);
    }


    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra("task", mTasks.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
    }

    private void deleteNote(Task task) {
        mTasks.remove(task);
        mTaskAdapter.notifyDataSetChanged();

        mTaskRepository.deleteTask(task);
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            deleteNote(mTasks.get(viewHolder.getAdapterPosition()));
        }
    };
}

