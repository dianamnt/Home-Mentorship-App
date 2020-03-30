package com.example.studentmanagement.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.studentmanagement.R
import com.example.studentmanagement.STUDENT_POSITION
import com.example.studentmanagement.model.Student
import org.w3c.dom.Text

class MyAdapter(private val context: Context, private val studentList: List<Student>) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = layoutInflater.inflate(R.layout.layout_item, parent,false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myStudent = studentList[position]
        holder.studentName.text = myStudent.name
        holder.studentPlace.text = myStudent.place
        holder.studentDate.text = myStudent.dateTime
        holder.studentPosition = position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentName = itemView.findViewById<TextView>(R.id.name)
        val studentPlace = itemView.findViewById<TextView>(R.id.place)
        val studentDate = itemView.findViewById<TextView>(R.id.dateTime)

        var studentPosition = 0

        init {
            if(isOnline(context))
            {
                itemView.findViewById<LinearLayout>(R.id.card).setOnClickListener {
                    val activityIntent = Intent(context, EditActivity::class.java)
                    activityIntent.putExtra(STUDENT_POSITION, studentPosition)
                    context.startActivity(activityIntent)
                }
                itemView.findViewById<AppCompatButton>(R.id.btnEdit).setOnClickListener {
                    val activityIntent = Intent(context, EditActivity::class.java)
                    activityIntent.putExtra(STUDENT_POSITION, studentPosition)
                    context.startActivity(activityIntent)
                }
                itemView.findViewById<AppCompatButton>(R.id.btnDelete).setOnClickListener {
                    val activityIntent = Intent(context, DeleteActivity::class.java)
                    activityIntent.putExtra(STUDENT_POSITION, studentPosition)
                    context.startActivity(activityIntent)
                }
            }
            else {
                itemView.findViewById<AppCompatButton>(R.id.btnDelete)?.isEnabled = false
                itemView.findViewById<AppCompatButton>(R.id.btnEdit)?.isEnabled = false
            }
        }

        fun isOnline(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }
}