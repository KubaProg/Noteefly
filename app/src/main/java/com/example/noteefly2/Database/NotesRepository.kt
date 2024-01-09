package com.example.noteefly2.Database

import androidx.lifecycle.LiveData
import com.example.noteefly2.Models.Note

class NotesRepository(private val noteDao: NoteDao) {

    val allNotes : LiveData<List<Note>> = noteDao.getAllNotes();

    suspend fun insert(note: Note){
        noteDao.insert(note)
    }

    suspend fun delete(note: Note){
        noteDao.delete(note)
    }

    suspend fun update(note: Note){
        noteDao.upgrade(note.id,note.title,note.note)
    }

}