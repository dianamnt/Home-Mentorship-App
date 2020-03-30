package com.example.studentmanagement.db

import androidx.room.*
import com.example.studentmanagement.model.Student

@Dao
interface StudentDao {
    @Query("SELECT * FROM student")
    fun getStudentList(): MutableList<Student>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStudent(student: Student)

    @Delete
    fun deleteStudent(student: Student)

    @Query("UPDATE student SET name= :name, place= :place, dateTime= :dateTime  WHERE name= :initialName")
    fun update(initialName: String, name: String, place: String, dateTime: String)
}