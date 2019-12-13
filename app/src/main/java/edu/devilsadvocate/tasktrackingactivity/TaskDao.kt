package edu.devilsadvocate.tasktrackingactivity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task_table WHERE taskCompletionStatus = 0")
    fun getTasksOrderedByDate(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()

    @Query("SELECT SUM(taskTimeToCompletionInMinutes) FROM task_table")
    fun getSectionTime(): Int
}