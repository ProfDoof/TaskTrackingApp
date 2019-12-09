package edu.devilsadvocate.tasktrackingactivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.time.Instant
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var taskViewModel: TaskViewModel

    private val newTaskActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = TaskListAdapter(this)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        taskViewModel.allTasks.observe(this, Observer { tasks ->
            tasks?.let { adapter.setTasks(it) }
        })

        fab_add_new_task.setOnClickListener {
            val intent = Intent(this@MainActivity, NewTaskActivity::class.java)
            startActivityForResult(intent, newTaskActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if( requestCode == newTaskActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.run {
                val taskName = getStringExtra(NewTaskActivity.EXTRA_TASK_NAME)
                val taskDesc = getStringExtra(NewTaskActivity.EXTRA_TASK_DESC)
                val taskTargetDate: Date = Date(getLongExtra(NewTaskActivity.EXTRA_TASK_TARGET_DATE, 0))
                val taskTimeToCompletion = getIntExtra(NewTaskActivity.EXTRA_TASK_TIME_TO_COMPLETION, 0)
                val task = Task(id = null, taskName = taskName ?: "", taskDescription = taskDesc ?: "", taskCompletionStatus = false, taskTargetCompletionDate = taskTargetDate, taskTimeToCompletionInMinutes = taskTimeToCompletion)
                taskViewModel.run {
                    insert(task)
                }
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.not_saved,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}


