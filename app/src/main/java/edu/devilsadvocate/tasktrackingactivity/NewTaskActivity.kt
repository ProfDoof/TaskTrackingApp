package edu.devilsadvocate.tasktrackingactivity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_new_task.*

class NewTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)

        button_save.setOnClickListener {
            val replyIntent = Intent()
            val isEditTaskNameEmpty = TextUtils.isEmpty(edit_task_name.text)
            val isEditTaskDescEmpty = TextUtils.isEmpty(edit_task_desc.text)
            if(isEditTaskNameEmpty || (isEditTaskNameEmpty && isEditTaskDescEmpty) )
                setResult(Activity.RESULT_CANCELED, replyIntent)
            else {
                val taskName = edit_task_name.text.toString()
                val taskDesc = edit_task_desc.text.toString()
                replyIntent.putExtra(EXTRA_TASK_NAME, taskName)
                replyIntent.putExtra(EXTRA_TASK_DESC, taskDesc)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_TASK_NAME = "edu.devilsadvocate.tasktrackingapp.TASK_NAME"
        const val EXTRA_TASK_DESC = "edu.devilsadvocate.tasktrackingapp.TASK_DESC"
    }
}
