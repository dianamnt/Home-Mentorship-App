package com.example.studentmanagement.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studentmanagement.model.Student

@Database(entities = [Student::class], version = 1)
abstract class StudentManagementDatabase: RoomDatabase() {
    abstract fun studentDao(): StudentDao

    companion object {
        private var instance: StudentManagementDatabase? = null

        fun getInstance(context: Context): StudentManagementDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudentManagementDatabase::class.java,
                    "StudentManagementDatabase"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance as StudentManagementDatabase
        }
    }
}