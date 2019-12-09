package edu.devilsadvocate.tasktrackingactivity

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@Database(entities = arrayOf(Task::class), version = 2, exportSchema = false)
@TypeConverters(Converters::class)
public abstract class TaskRoomDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    private class TaskDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.taskDao())
                }
            }
        }

        suspend fun populateDatabase(taskDao: TaskDao) {
            taskDao.deleteAll()

            var task = Task(id = null, taskName = "Test Task", taskDescription = "Testing out the task", taskCompletionStatus = false, taskTargetCompletionDate = Date.from(
                Instant.now()), taskTimeToCompletionInMinutes = 5)
            taskDao.insert(task)

        }
    }

    companion object {
        @Volatile
        private var INSTANCE: TaskRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TaskRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        TaskRoomDatabase::class.java,
                        "task_database"
                    ).addCallback(TaskDatabaseCallback(scope)).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                return instance
            }
        }
    }
}