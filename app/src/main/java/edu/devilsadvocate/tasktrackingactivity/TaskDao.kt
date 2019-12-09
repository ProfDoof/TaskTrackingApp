package edu.devilsadvocate.tasktrackingactivity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.time.LocalDate
import java.util.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task_table WHERE taskCompletionStatus = 0") //WHERE taskTargetCompletionDate = date
    fun getTasksOrderedByDate(): LiveData<List<Task>> //(date: Date)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()
}