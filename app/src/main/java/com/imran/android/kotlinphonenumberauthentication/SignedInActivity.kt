package com.imran.android.kotlinphonenumberauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SignedInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signed_in)
    }

    public fun actionSignOut(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }
}