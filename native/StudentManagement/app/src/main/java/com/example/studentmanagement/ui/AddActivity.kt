package com.example.studentmanagement.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.studentmanagement.ADD_STUDENT_URL
import com.example.studentmanagement.R
import com.example.studentmanagement.db.StudentManagementDatabase
import com.example.studentmanagement.model.Student
import com.example.studentmanagement.repository.Repository
import com.example.studentmanagement.server.VolleySingleton
import kotlinx.android.synthetic.main.activity_add.*
import org.json.JSONObject


class AddActivity : AppCompatActivity() {

    private lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        textView.visibility = View.VISIBLE
        textView.startAnimation(animation)
        addName.visibility = View.VISIBLE
        addName.startAnimation(animation)
        addPlace.visibility = View.VISIBLE
        addPlace.startAnimation(animation)
        addDate.visibility = View.VISIBLE
        addDate.startAnimation(animation)
        btnSubmitAdd.visibility = View.VISIBLE
        btnSubmitAdd.startAnimation(animation)

        repository = Repository(StudentManagementDatabase.getInstance(this).studentDao())

        submitAddUI()
    }

    fun submitAddUI() {
        btnSubmitAdd.setOnClickListener {
            val studentName = addName.text.toString()
            val studentPlace = addPlace.text.toString()
            val studentDate = addDate.text.toString()
            val new_student = Student(studentName,studentPlace,studentDate)

            repository.add(new_student)
            addRemote(studentName, studentPlace, studentDate)

            val activityIntent = Intent(this, MainScreenActivity::class.java)
            startActivity(activityIntent)
        }
    }

    fun addRemote(studentName: String, studentPlace: String, studentDate: String) {
        val params = JSONObject()
        params.put("name", studentName)
        params.put("location", studentPlace)
        params.put("dateTime", studentDate)

        val request = JsonObjectRequest(Request.Method.POST, ADD_STUDENT_URL, params,
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
