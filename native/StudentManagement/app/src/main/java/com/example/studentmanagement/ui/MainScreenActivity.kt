package com.example.studentmanagement.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.studentmanagement.ADD_STUDENT_URL
import com.example.studentmanagement.DELETE_STUDENT_URL
import com.example.studentmanagement.GET_STUDENTS_URL
import com.example.studentmanagement.R
import com.example.studentmanagement.db.StudentManagementDatabase
import com.example.studentmanagement.model.Student
import com.example.studentmanagement.repository.Repository
import com.example.studentmanagement.server.VolleySingleton
import kotlinx.android.synthetic.main.activity_main_screen.*
import org.json.JSONArray
import org.json.JSONObject

import java.util.*

class MainScreenActivity : AppCompatActivity() {

    private lateinit var repository: Repository
    private val remoteList = ArrayList<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        repository = Repository(StudentManagementDatabase.getInstance(this).studentDao())

        getRemote()

        setUpRecyclerView()

        addUI()
    }

    private fun setUpRecyclerView() {
        studentListRecyclerView.layoutManager = LinearLayoutManager(this)
        val studentList = repository.getAllStudents() ?: listOf<Student>()
        studentListRecyclerView.adapter = MyAdapter(this, studentList)
    }

    override fun onResume() {
        super.onResume()
        getRemote()
        setUpRecyclerView()
    }

    override fun onRestart() {
        super.onRestart()
        getRemote()
        setUpRecyclerView()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        getRemote()
        setUpRecyclerView()
        super.onWindowFocusChanged(hasFocus)
    }

    fun addUI() {
        btnAdd.setOnClickListener {
            val activityIntent = Intent(this, AddActivity::class.java)
            startActivity(activityIntent)
        }
    }

    private fun getRemote() {
        val request = StringRequest(Request.Method.GET, GET_STUDENTS_URL,
            Response.Listener { response ->
                // Process the json
                try {

                    var strResp = response.toString()
                    val jsonObj: JSONObject = JSONObject(strResp)
                    val jsonArray: JSONArray = jsonObj.getJSONArray("students")
                    var str_user: String = ""
                    for (i in 0 until jsonArray.length()) {
                        var jsonInner: JSONObject = jsonArray.getJSONObject(i)
                        val student = Student(jsonInner.get("name").toString(),
                            jsonInner.get("location").toString(), jsonInner.get("dateTime").toString())
                        remoteList.add(student)
                        str_user = str_user + "\n" + jsonInner.toString()

                    }
                    updateRemote()
                    Log.d("...", str_user)
                } catch (e: Exception) {
                    Log.d("response", e.toString())
                }

            }, Response.ErrorListener {
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

    private fun updateRemote(): MutableList<Student> {
        val localStudents = repository.getAllStudents()

        for (ls in localStudents) {
            var found = false
            for (student in remoteList) {
                if (ls.name == student.name) {
                    found = true
                    break
                }
            }
            if (!found) {
                add(ls)
            }
        }
        return localStudents
    }

    private fun add(student : Student) {
        val params = JSONObject()
        params.put("name", student.name)
        params.put("location", student.place)
        params.put("dateTime", student.dateTime)

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



