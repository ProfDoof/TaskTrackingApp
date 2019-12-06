package edu.devilsadvocate.tasktrackingactivity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {

    @Query("SELECT * FROM task_table ORDER BY taskTargetCompletionDate ASC")
    fun getTasksOrderedByDate(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()
}