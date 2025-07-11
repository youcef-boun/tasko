package com.youcef_bounaas.tasko.Main.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task (
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val title : String,
    val description : String,
    val priority : Int,
    val dueDate : String,
    var isCompleted : Boolean,

    val imagePath: String





)