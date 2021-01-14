package com.imran.android.kotlinphonenumberauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class SignedInActivity : AppCompatActivity() {
    private val NUMBER_EXTRA = "NUMBER_EXTRA"
    private lateinit var textNumberSignedIn: TextView
    private lateinit var phoneNumber: String

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signed_in)

        textNumberSignedIn = findViewById(R.id.textNumberSigned)
        firebaseAuth = FirebaseAuth.getInstance()

        phoneNumber = intent.getStringExtra(NUMBER_EXTRA).toString()
        textNumberSignedIn.text = phoneNumber
    }

    public fun actionSignOut(view: View) {
        firebaseAuth.signOut()
        startActivity(Intent(this, MainActivity::class.java))
    }
}












