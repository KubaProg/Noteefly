package com.example.noteefly2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.noteefly2.Models.Note
import com.example.noteefly2.databinding.ActivityMainBinding

class AddNote : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var note : Note;
    private lateinit var old_note: Note
    var isUpdate = false;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_add_note)
    }
}