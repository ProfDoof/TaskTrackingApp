package edu.devilsadvocate.tasktrackingactivity

import androidx.lifecycle.LiveData

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: LiveData<List<Task>> = taskDao.getTasksOrderedByDate()

    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }
}