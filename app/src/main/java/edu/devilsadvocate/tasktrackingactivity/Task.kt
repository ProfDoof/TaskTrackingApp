package edu.devilsadvocate.tasktrackingactivity

import android.app.ActivityManager
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "task_table")
data class Task (
    @PrimaryKey(autoGenerate = true)
    var id: Int?,

    @ColumnInfo(name = "taskName")
    var taskName: String,

    @ColumnInfo(name= "taskDescription")
    var taskDescription: String,

    @ColumnInfo(name = "taskCompletionStatus")
    var taskCompletionStatus: Boolean,

    @ColumnInfo(name = "taskTargetCompletionDate")
    var taskTargetCompletionDate: Date
)
