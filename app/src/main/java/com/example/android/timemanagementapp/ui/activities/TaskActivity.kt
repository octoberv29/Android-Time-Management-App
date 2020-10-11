package com.example.android.timemanagementapp.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android.timemanagementapp.R
import com.example.android.timemanagementapp.models.Task
import com.example.android.timemanagementapp.persistence.TaskRepository
import kotlinx.android.synthetic.main.activity_edit_task.*
import kotlinx.android.synthetic.main.content_edit_task.*
import java.util.*

class TaskActivity : AppCompatActivity() {

    private lateinit var mTaskRepository: TaskRepository
    private lateinit var task: Task
    private var isNewTask = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        setSupportActionBar(editToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mTaskRepository = TaskRepository(this)

        if (intent.hasExtra("selected_task")) {
            getExistingTask()
            isNewTask = false
            if (task.title != null) {
                supportActionBar!!.title = task.title
            }
        } else {
            getNewTask()
            isNewTask = true
            supportActionBar!!.title = "New Task"
        }

        saveTaskBtn.setOnClickListener { v ->
            saveTask()
            finish()
        }

    }

    private fun getExistingTask() {
        task = intent.getParcelableExtra("selected_task")
        titleEdit.setText(task.title)
        description.setText(task.description)
    }

    private fun getNewTask() {
        val title = ""
        val description = ""
        val created_at = Calendar.getInstance().timeInMillis
        val completed = false
        val priority = false
        val due_date = Calendar.getInstance().timeInMillis
        val reminder = false
        val remind_at = Calendar.getInstance().timeInMillis
        task = Task(title, description, created_at, completed, priority, due_date, reminder, remind_at)
    }

    private fun saveTask() {
        val title: String = java.lang.String.valueOf(titleEdit.text).trim { it <= ' ' }
        val description: String = java.lang.String.valueOf(description).trim { it <= ' ' }
        task.title = title
        task.description = description
        if (isNewTask) {
            mTaskRepository.insertTask(task)
        } else {
            mTaskRepository.updateTask(task)
        }
    }
}