package com.example.studentmanagement.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.studentmanagement.ADD_STUDENT_URL
import com.example.studentmanagement.R
import com.example.studentmanagement.STUDENT_POSITION
import com.example.studentmanagement.UPDATE_STUDENT_URL
import com.example.studentmanagement.db.StudentManagementDatabase
import com.example.studentmanagement.model.Student
import com.example.studentmanagement.repository.Repository
import com.example.studentmanagement.server.VolleySingleton

import kotlinx.android.synthetic.main.activity_edit.*
import org.json.JSONObject

class EditActivity : AppCompatActivity() {

    private lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        repository = Repository(StudentManagementDatabase.getInstance(this).studentDao())

        submitEditUI()
    }

    fun submitEditUI() {
        btnSubmitEdit.setOnClickListener {
            val studentName = editName.text.toString()
            val studentPlace = editPlace.text.toString()
            val studentDate = editDate.text.toString()
            val new_student = Student(studentName, studentPlace, studentDate)

            var bundle :Bundle ?=intent.extras
            var position = bundle!!.getInt(STUDENT_POSITION)

            var student = repository.findStudentByPosition(position)
            repository.update(student.name, new_student)
            editRemote(student.name, studentName, studentPlace, studentDate)

            val activityIntent = Intent(this, MainScreenActivity::class.java)
            startActivity(activityIntent)

        }
    }

    fun editRemote(studentOldName: String, studentName: String, studentPlace: String, studentDate: String) {
        val params = JSONObject()
        params.put("oldname", studentOldName)
        params.put("name", studentName)
        params.put("location", studentPlace)
        params.put("dateTime", studentDate)

        val request = JsonObjectRequest(Request.Method.POST, UPDATE_STUDENT_URL, params,
            Response.Listener { response ->
                // Process the json
                try {
                    Log.d("response", "[" + response + "]")
                }catch (e:Exception){
                    Log.d("response", e.toString())
                }

            }, Response.ErrorListener{
                // Error in request
                Log.d("response", it.toString())
            })


        // Volley request policy, only one time request to avoid duplicate transaction
        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            // 0 means no retry
            0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
            1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        // Add the volley post request to the request queue
        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }
}
