package com.willklep.remindcurve

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UpdateActivity : AppCompatActivity() {
    // var updateImage: ImageView? = null
    private var updateButton: Button? = findViewById<Button>(R.id.updateButton)
    //var updateDesc: EditText? = null
    var updateTitle: EditText? = findViewById<EditText>(R.id.updateTitle)
    // var updateLang: EditText? = null
    var title: String? = null
    //var desc: String? = null
    //var lang: String? = null
    //var imageUrl: String? = null
    private var key: String? = null
    // var oldImageURL: String? = null
    var uri: Uri? = null
    var databaseReference: DatabaseReference? = null
    var storageReference: StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        //updateButton
        // updateDesc = findViewById<EditText>(R.id.updateDesc)
        // updateImage = findViewById<ImageView>(R.id.updateImage)
        // updateLang = findViewById<EditText>(R.id.updateLang)
        // updateTitle =
        val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                uri = data!!.data
                // updateImage.setImageURI(uri)
            } else {
                Toast.makeText(this@UpdateActivity, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }
        val bundle = intent.extras
        if (bundle != null) {
            //Glide.with(this@UpdateActivity).load(bundle.getString("Image")).into(updateImage)
            updateTitle?.setText(bundle.getString("Title"))
//            updateDesc.setText(bundle.getString("Description"))
//            updateLang.setText(bundle.getString("Language"))
            key = bundle.getString("Key")
            // oldImageURL = bundle.getString("Image")
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Android Tutorials").child(
            key!!
        )
//        updateImage.setOnClickListener(View.OnClickListener {
//            val photoPicker = Intent(Intent.ACTION_PICK)
//            photoPicker.type = "image/*"
//            activityResultLauncher.launch(photoPicker)
//        })
        updateButton?.setOnClickListener(View.OnClickListener {
            saveData()
            val intent = Intent(this@UpdateActivity, HomeController::class.java)
            startActivity(intent)
        })
    }

    private fun saveData() {
        storageReference = FirebaseStorage.getInstance().reference.child("Android Images").child(
            uri!!.lastPathSegment!!
        )
        val builder = AlertDialog.Builder(this@UpdateActivity)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()
        storageReference!!.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isComplete);
            val urlImage = uriTask.result
           // imageUrl = urlImage.toString()
            updateData()
            dialog.dismiss()
        }.addOnFailureListener { dialog.dismiss() }
    }

    fun updateData() {
        title = updateTitle!!.text.toString().trim { it <= ' ' }
//        desc = updateDesc!!.text.toString().trim { it <= ' ' }
//        lang = updateLang!!.text.toString()
        val dataClass = Task(title)
        databaseReference!!.setValue(dataClass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
//                val reference = FirebaseStorage.getInstance().getReferenceFromUrl(
//                    oldImageURL!!
//                )
//                reference.delete()
                Toast.makeText(this@UpdateActivity, "Updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(
                this@UpdateActivity,
                e.message.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}