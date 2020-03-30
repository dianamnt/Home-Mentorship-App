package com.example.studentmanagement.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "student")
data class Student(
    @PrimaryKey var name: String,
    var place: String,
    var dateTime: String
)