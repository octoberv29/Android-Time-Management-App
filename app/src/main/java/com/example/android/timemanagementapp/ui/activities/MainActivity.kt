package com.example.android.timemanagementapp.ui.activities

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.timemanagementapp.R
import com.example.android.timemanagementapp.models.Task
import com.example.android.timemanagementapp.persistence.TaskRepository
import com.example.android.timemanagementapp.ui.adapters.TaskAdapter
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        TaskAdapter.OnTaskClickListener {

    private lateinit var drawerToggle: ActionBarDrawerToggle

    private lateinit var mTaskAdapter: TaskAdapter
    private lateinit var mTaskRepository: TaskRepository
    private var mTasks = ArrayList<Task>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.menu_inbox)

        setSupportActionBar(mainToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = "Inbox"

        drawerToggle = ActionBarDrawerToggle(this, drawerLayout,R.string.open, R.string.close)
        drawerLayout.addDrawerListener(drawerToggle)

        recyclerTasks.layoutManager = LinearLayoutManager(this)
        recyclerTasks.setHasFixedSize(true)
        mTaskAdapter = TaskAdapter(this, mTasks, this)
        recyclerTasks.adapter = mTaskAdapter
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var taskToDelete: Task = mTasks[viewHolder.adapterPosition]
                // TODO: why do I delete it from tasks and adapter ??
                mTasks.remove(taskToDelete)
                mTaskAdapter.notifyDataSetChanged()
                mTaskRepository.deleteTask(taskToDelete)
            }

        }).attachToRecyclerView(recyclerTasks)

        addTaskBtn.setColorFilter(Color.WHITE)
        addTaskBtn.setOnClickListener { view ->
            val intent = Intent(this, TaskActivity::class.java)
            startActivity(intent)
        }

        mTaskRepository = TaskRepository(this)
        retrieveTasks()
    }

    private fun retrieveTasks() {
        mTaskRepository.retrieveTasks().observe(this, Observer { tasks: List<Task>? ->
            mTasks.clear()
            if (tasks != null) {
                mTasks.addAll(tasks)
            }
            mTaskAdapter.notifyDataSetChanged()
        })
    }

    // --- Methods for Drawer Layout ---
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

    // --- Methods for NavigationView.OnNavigationItemSelectedListener ---
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_priority -> {
                supportActionBar!!.title = "Priority"
                drawerLayout.closeDrawers()
                mTaskRepository.retrievePriorityTasks().observe(this, Observer { tasks: List<Task>? ->
                    mTasks.clear()
                    if (tasks != null) {
                        mTasks.addAll(tasks)
                    }
                    mTaskAdapter.notifyDataSetChanged()
                })
                return true
            }
            R.id.menu_inbox -> {
                supportActionBar!!.title = "Inbox"
                drawerLayout.closeDrawers()
                mTaskRepository.retrieveTasks().observe(this, Observer { tasks: List<Task>? ->
                    mTasks.clear()
                    if (tasks != null) {
                        mTasks.addAll(tasks)
                    }
                    mTaskAdapter.notifyDataSetChanged()
                })
                return true
            }
            R.id.menu_completed -> {
                supportActionBar!!.title = "Completed"
                drawerLayout.closeDrawers()
                mTaskRepository.retrieveCompletedTasks().observe(this, Observer { tasks: List<Task>? ->
                    mTasks.clear()
                    if (tasks != null) {
                        mTasks.addAll(tasks)
                    }
                    mTaskAdapter.notifyDataSetChanged()
                })
                return true
            }
        }
        return false
    }

    // --- Methods for TaskAdapter.OnTaskClickListener ---
    override fun onClick(position: Int) {
        var task: Task = mTasks[position]
        var intent = Intent(this, TaskActivity::class.java)
        intent.putExtra("selected_task", task)
        startActivity(intent)
    }
}