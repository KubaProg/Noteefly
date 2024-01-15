package com.example.noteefly2.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note (
    @PrimaryKey(autoGenerate = true) val id : Int?,
    @ColumnInfo(name="title") val title : String?,
    @ColumnInfo(name="note") val note: String?,
    @ColumnInfo(name="date") val date : String?,
    @ColumnInfo(name="category") val category : String?,
    @ColumnInfo(name="importanceLevel") val importanceLevel : String?
) :java.io.Serializable
