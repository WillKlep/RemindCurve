package com.willklep.remindcurve;

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate


class UploadActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    private lateinit var topicEditText: EditText
    private lateinit var courseEditText: EditText
    private lateinit var startDatePickerButton: Button
    private lateinit var endDatePickerButton: Button
    private lateinit var uploadButton: Button


    private var selectedClass: String? = null
    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        topicEditText = findViewById(R.id.uploadTopic)
        courseEditText = findViewById<EditText>(R.id.upload_course)
        startDatePickerButton = findViewById<Button?>(R.id.startDatePickerButton)
        startDatePickerButton.text = LocalDate.now().toString()
        endDatePickerButton = findViewById(R.id.endDatePickerButton)
        endDatePickerButton.text = LocalDate.now().toString()
        uploadButton = findViewById(R.id.saveButton)

        // Populate classSpinner with data from Firebase
        database = FirebaseDatabase.getInstance().reference

        // Set onClickListener for startDatePickerButton
        startDatePickerButton.setOnClickListener {
            val currentDate = LocalDate.now()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    startDate = LocalDate.of(year, month + 1, dayOfMonth)
                    startDatePickerButton.text = startDate.toString()
                },
                currentDate.year,
                currentDate.monthValue - 1,
                currentDate.dayOfMonth
            )
            datePicker.show()
        }

        // Set onClickListener for endDatePickerButton
        endDatePickerButton.setOnClickListener {
            val currentDate = LocalDate.now()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    endDate = LocalDate.of(year, month + 1, dayOfMonth)
                    endDatePickerButton.text = endDate.toString()
                },
                currentDate.year,
                currentDate.monthValue - 1,
                currentDate.dayOfMonth
            )
            datePicker.show()
        }

        // Set onClickListener for uploadButton
        uploadButton.setOnClickListener {
            val topic = topicEditText.text.toString().trim()
            selectedClass = courseEditText.text.toString().trim()
            if (selectedClass != null && topic.isNotEmpty() && startDate != null && endDate != null) {
                val task = Task(topic = topic, className = selectedClass!!, startDate = startDate!!,
                    endDate = endDate!!)
                // Upload task to Firebase
                val taskRef = database.child("users").child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child("tasks").push()
                taskRef.setValue(task)
                    .addOnCompleteListener{task ->
                        if(task.isSuccessful){
                            val i = Intent(this, HomeController::class.java)
                            startActivity(i)
                        }
                        else{
                            Toast.makeText(this, task.exception?.message,Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
