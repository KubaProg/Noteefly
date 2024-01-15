package com.example.noteefly2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val importanceLevels = resources.getStringArray(R.array.importance_levels)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, importanceLevels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerImportanceLevel.adapter = adapter

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
        }

        binding.imgBackArrow.setOnClickListener {
            onBackPressed()
        }
    }
}
