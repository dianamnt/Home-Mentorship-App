package com.example.studentmanagement.repository

import com.example.studentmanagement.db.StudentDao
import com.example.studentmanagement.model.Student
import java.util.*
import kotlin.collections.ArrayList

class Repository(private val studentDao: StudentDao) {

    fun getAllStudents(): MutableList<Student> {
        return studentDao.getStudentList()
    }

    fun add(new_student: Student) {
        studentDao.insertStudent(new_student)
    }

    fun delete(student: Student) {
        studentDao.deleteStudent(student)
    }

    fun findStudentByPosition(position: Int): Student {
        val list: MutableList<Student> = getAllStudents()
        var i: Int = 0;
        list.forEach{ s->
            if(i == position)
            {
                return s
            }
            i = i+1
        }
        return Student("", "", "")
    }

    fun update(initialName: String, new_student: Student) {
        studentDao.update(initialName, new_student.name, new_student.place, new_student.dateTime)
    }

}