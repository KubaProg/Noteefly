package com.example.noteefly2

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.noteefly2.Models.Note
import com.example.noteefly2.databinding.ActivityAddNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class AddNote : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding

    private lateinit var note: Note
    private lateinit var old_note: Note
    var isUpdate = false
    private lateinit var timePicker: TimePicker
    private lateinit var importancePicker: Spinner
    private lateinit var checkBox: CheckBox
    private lateinit var timeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val importanceLevels = resources.getStringArray(R.array.importance_levels)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, importanceLevels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerImportanceLevel.adapter = adapter

        timePicker = findViewById(R.id.time_picker)
        importancePicker = findViewById(R.id.spinner_importance_level)
        timePicker.setIs24HourView(true)
        timeText = findViewById(R.id.tv_set_notification_time)

        importancePicker.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position > 3) {
                    timeText.visibility = View.VISIBLE
                    timePicker.visibility = View.VISIBLE
                } else {
                    timeText.visibility = View.GONE
                    timePicker.visibility = View.GONE
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // You can leave this empty if you don't need any action when nothing is selected
            }
        }

        if (intent.hasExtra("current_note")) {
            old_note = intent.getSerializableExtra("current_note") as Note
            binding.etTitle.setText(old_note.title)
            binding.etNote.setText(old_note.note)
            binding.etCategory.setText(old_note.category)
            binding.spinnerImportanceLevel.setSelection(importanceLevels.indexOf(old_note.importanceLevel))
            isUpdate = true
        }

        binding.imgCheck.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val noteDesc = binding.etNote.text.toString()
            val category = binding.etCategory.text.toString()
            val importanceLevel = binding.spinnerImportanceLevel.selectedItem.toString()

            if (title.isNotEmpty() && noteDesc.isNotEmpty()) {
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a", Locale.getDefault())
                val formattedDate = formatter.format(Date())

                note = if (isUpdate) {
                    Note(old_note.id, title, noteDesc, formattedDate, category, importanceLevel)
                } else {
                    Note(null, title, noteDesc, formattedDate, category, importanceLevel)
                }

                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this@AddNote, "Proszę podać dane", Toast.LENGTH_SHORT).show()
            }

            if (importanceLevel == "5") {
                scheduleNotification()
            }
        }

        binding.imgBackArrow.setOnClickListener {
            onBackPressed()
        }
    }

    private fun scheduleNotification() {
        val intent = Intent(applicationContext, NotifyNote::class.java)
        val title = binding.etTitle.text.toString()
        val note = binding.etNote.text.toString()
        intent.putExtra(notifTitle, title)
        intent.putExtra(notifNote, note)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val notifHour = timePicker.hour
        val notifMinute = timePicker.minute
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, notifHour)
        calendar.set(Calendar.MINUTE, notifMinute)
        calendar.set(Calendar.SECOND, 0)
        val repeatInterval = AlarmManager.INTERVAL_DAY

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                val canScheduleExact = alarmManager.canScheduleExactAlarms()
                if (canScheduleExact) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        repeatInterval,
                        pendingIntent
                    )
                } else {

                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    repeatInterval,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            // Handle SecurityException
            // This may occur if your app doesn't have the necessary permission
        }
    }
}
