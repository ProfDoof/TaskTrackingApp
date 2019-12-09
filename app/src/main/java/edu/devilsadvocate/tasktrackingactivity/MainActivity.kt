package edu.devilsadvocate.tasktrackingactivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import edu.devilsadvocate.tasktrackingactivity.ItemTouchHelpers.SimpleItemTouchHelperCallback
import kotlinx.android.synthetic.main.activity_main.*
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import java.util.*

class MainActivity : AppCompatActivity(), TasksSection.ClickListener {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var sectionedAdapter: SectionedRecyclerViewAdapter
    private var itemTouchHelper: ItemTouchHelper? = null

    private val newTaskActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        generateSectionedRecyclerView(emptyList())

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        taskViewModel.allTasks.observe(this, Observer { tasks ->
            tasks?.let { generateSectionedRecyclerView(it) }
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

    private fun generateSectionedRecyclerView(tasks: List<Task>) {
        val splitTasks = tasks.groupBy { it.taskTargetCompletionDate }.toSortedMap()
        val adaptersList: MutableList<TasksSection> = emptyList<TasksSection>().toMutableList()
        sectionedAdapter = SectionedRecyclerViewAdapter()

        for (split in splitTasks) {
            val taskSection: TasksSection = TasksSection(split.key.toString(), split.value.toMutableList(), this)
            adaptersList.add(taskSection)
            sectionedAdapter.addSection(taskSection)
        }

        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(adaptersList)
        if (itemTouchHelper != null)
            itemTouchHelper?.attachToRecyclerView(null)
        itemTouchHelper = ItemTouchHelper(callback)
        recyclerview.adapter = sectionedAdapter
        recyclerview.layoutManager = LinearLayoutManager(this)
        itemTouchHelper?.attachToRecyclerView(recyclerview)
    }

    override fun onItemRootViewClicked(sectionTitle: String, itemAdapterPosition: Int) {
        Toast.makeText(
            this,
            String.format(
                "Clicked on position #%s of Section %s",
                sectionedAdapter.getPositionInSection(itemAdapterPosition),
                sectionTitle
            ),
            Toast.LENGTH_SHORT
        ).show();
    }

    override fun onTaskCompletion(task: Task) {
        taskViewModel.complete(task)
    }
}


