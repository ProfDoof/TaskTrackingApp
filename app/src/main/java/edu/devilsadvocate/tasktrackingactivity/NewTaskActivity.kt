package edu.devilsadvocate.tasktrackingactivity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_new_task.*
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class NewTaskActivity : AppCompatActivity() {

    private var targetDate: Date? = null

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
                val taskTimeToCompletion = num_minutes_to_finish.text.toString().toInt()

                replyIntent.putExtra(EXTRA_TASK_TIME_TO_COMPLETION, taskTimeToCompletion)
                replyIntent.putExtra(EXTRA_TASK_TARGET_DATE, targetDate?.time)
                replyIntent.putExtra(EXTRA_TASK_NAME, taskName)
                replyIntent.putExtra(EXTRA_TASK_DESC, taskDesc)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

        date_picker.setOnClickListener {
            clickDataPicker()
        }
    }

    @SuppressWarnings("unused")
    private fun clickDataPicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, yearSelected, monthOfYear, dayOfMonth ->
            date_picker.setText("""${monthOfYear + 1}/$dayOfMonth/$yearSelected""")
            targetDate = Date.from(LocalDate.of(yearSelected, monthOfYear+1, dayOfMonth).atStartOfDay(
                ZoneId.systemDefault()).toInstant())
        }, year, month, day)
        dpd.show()
    }

    companion object {
        const val EXTRA_TASK_TIME_TO_COMPLETION = "edu.devilsadvocate.tasktrackingapp.TASK_TIME_TO_COMPLETION"
        const val EXTRA_TASK_TARGET_DATE = "edu.devilsadvocate.tasktrackingapp.TASK_TARGET_DATE"
        const val EXTRA_TASK_NAME = "edu.devilsadvocate.tasktrackingapp.TASK_NAME"
        const val EXTRA_TASK_DESC = "edu.devilsadvocate.tasktrackingapp.TASK_DESC"
    }
}
