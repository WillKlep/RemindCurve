package com.willklep.remindcurve

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class SignInController : AppCompatActivity() {
    private lateinit var signInWithGoogleTextView: TextView
    private lateinit var client: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            val i = Intent(this, HomeController::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        } else {
            // User is signed out
            Log.d("Signout:", "onAuthStateChanged:signed_out")
        }
        setContentView(R.layout.activity_signin)
        signInWithGoogleTextView = findViewById(R.id.signInWithGoogle)
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        client = GoogleSignIn.getClient(this, options)
        signInWithGoogleTextView.setOnClickListener{
            val intent = client.signInIntent
            startActivityForResult(intent,10001)
        }
    } // end onCreate

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 10001){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken,null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        val i = Intent(this, UploadActivity::class.java)
                        startActivity(i)
                    }
                    else{
                        Toast.makeText(this, task.exception?.message,Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }
} // end of class