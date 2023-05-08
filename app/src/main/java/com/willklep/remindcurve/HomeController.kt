package com.willklep.remindcurve

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.willklep.remindcurve.UploadActivity
import java.util.Locale

class HomeController : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var addFab: FloatingActionButton
    private lateinit var adapter: TaskAdapter
    private lateinit var taskList: MutableList<Task>

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_controller)

        // Initialize Firebase components
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView)
        addFab = findViewById(R.id.addFab)

        // Initialize adapter and task list
        taskList = mutableListOf()
        adapter = TaskAdapter(taskList)
        recyclerView.adapter = adapter

        // Set layout manager
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Add click listener to FAB
        addFab.setOnClickListener {
            // Launch AddTaskActivity
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }

        // Check if user is logged in and retrieve their task data from Firebase
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val taskRef = database.getReference("users/$uid/tasks")

            taskRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Clear taskList before adding new tasks
                    taskList.clear()

                    // Loop through snapshot and add tasks to taskList
                    for (taskSnapshot in snapshot.children) {
                        val task = taskSnapshot.getValue(Task::class.java)
                        if (task != null) {
                            taskList.add(task)
                        }
                    }

                    // Notify adapter that data has changed
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("HomeController", "Firebase Database Error: ${error.message}")
                }
            })
       } // else {
//            // If user is not logged in, display message to add task
//            taskList.clear()
//            taskList.add(Task("Use the + to add a task"))
//            adapter.notifyDataSetChanged()
//        }
    }
}


