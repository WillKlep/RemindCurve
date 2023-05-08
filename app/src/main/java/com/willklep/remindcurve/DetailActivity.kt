package com.willklep.remindcurve

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.clans.fab.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.willklep.remindcurve.R

class DetailActivity : AppCompatActivity() {
    var detailDesc: TextView? = null
    private var detailTitle: TextView? = findViewById<TextView>(R.id.detailTitle)
    var detailLang: TextView? = null
    // var detailImage: ImageView? = null
    private var deleteButton: FloatingActionButton? = findViewById<FloatingActionButton>(R.id.deleteButton)
    private var editButton: FloatingActionButton? = findViewById<FloatingActionButton>(R.id.editButton)
    var key: String? = ""
    var imageUrl: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        // detailDesc = findViewById<TextView>(R.id.detailDesc)
        // detailImage = findViewById<ImageView>(R.id.detailImage)
        // deleteButton = findViewById<FloatingActionButton>(R.id.deleteButton)
        // editButton = findViewById<FloatingActionButton>(R.id.editButton)
        // detailLang = findViewById<TextView>(R.id.detailLang)
        val bundle = intent.extras
        if (bundle != null) {
           // detailDesc.setText(bundle.getString("Description"))
            detailTitle?.text = bundle.getString("Title")
            detailLang?.text = bundle.getString("Language")
            key = bundle.getString("Key")
            imageUrl = bundle.getString("Image")
            // Glide.with(this).load(bundle.getString("Image")).into(detailImage)
        }
        deleteButton?.setOnClickListener(View.OnClickListener {
            val reference = FirebaseDatabase.getInstance().getReference("Android Tutorials")
            val storage = FirebaseStorage.getInstance()
            val storageReference = storage.getReferenceFromUrl(imageUrl!!)
            storageReference.delete().addOnSuccessListener {
                reference.child(key!!).removeValue()
                Toast.makeText(this@DetailActivity, "Deleted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, HomeController::class.java))
                finish()
            }
        })
        editButton?.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@DetailActivity, UpdateActivity::class.java)
                .putExtra("Title", detailTitle?.text.toString())
                //.putExtra("Description", detailDesc.getText().toString())
                //.putExtra("Language", detailLang.getText().toString())
                //.putExtra("Image", imageUrl)
                .putExtra("Key", key)
            startActivity(intent)
        })
    }
}