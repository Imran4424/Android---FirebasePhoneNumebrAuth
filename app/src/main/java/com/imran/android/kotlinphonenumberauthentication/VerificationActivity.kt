package com.imran.android.kotlinphonenumberauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView

class VerificationActivity : AppCompatActivity() {
    private val NUMBER_EXTRA = "NUMBER_EXTRA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

    }

    public fun actionWrongNumber(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    public fun actionSubmit(view: View) {
        val signedInIntent = Intent(this, SignedInActivity::class.java)
//        signedInIntent.putExtra(NUMBER_EXTRA, phoneNumber)
        startActivity(signedInIntent)
    }
}