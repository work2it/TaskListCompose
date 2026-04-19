package ru.sergdeev.tasklist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_items")
data class DataItemDbModel(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        val title: String = "",
        val desc: String = "",
        val dt: Long = 0,
        val enabled: Boolean,
)