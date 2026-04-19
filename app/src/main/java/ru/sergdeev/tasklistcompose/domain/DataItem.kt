package ru.sergdeev.tasklistcompose.domain

data class DataItem (
    var id: Int = 0,
    var title: String = "",
    var desc: String = "",
    var dt: Long = 0,
    var enabled: Boolean,
)