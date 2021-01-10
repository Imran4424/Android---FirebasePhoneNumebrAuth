package com.imran.android.kotlinphonenumberauthentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner

class RegisterActivity : AppCompatActivity() {
    private val NUMBER_EXTRA = "NUMBER_EXTRA"
    private var spinner: Spinner
    private var editTextCountryCode: EditText
    private var editTextNumber: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }
}