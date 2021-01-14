package com.imran.android.kotlinphonenumberauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import java.util.*
import kotlin.collections.ArrayList

class RegisterActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val NUMBER_EXTRA = "NUMBER_EXTRA"
    private lateinit var spinnerCountry: Spinner
    private lateinit var editTextCountryCode: EditText
    private lateinit var editTextNumber: EditText
    private lateinit var countryCodesList: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        spinnerCountry = findViewById(R.id.spinnerCountry)
        spinnerCountry.onItemSelectedListener = this

        editTextCountryCode = findViewById(R.id.editTextCountryCode)
        editTextNumber = findViewById(R.id.editTextNumberRegister)

        countryCodesList =  resources.getStringArray(R.array.country_code)
        resources.getStringArray(R.array.country_code).also { countryCodesList = it }
    }

    private fun validatePhoneNumber(): Boolean {
        val enteredNumber = editTextNumber.text.toString()

        if (enteredNumber.isEmpty()) {
            editTextNumber.setError("Invalid Phone Number")
            return false
        }

        return true
    }

    public fun actionNext(view: View) {
        if (!validatePhoneNumber()) {
            return
        }

        val phoneNumber = editTextCountryCode.text.toString() + editTextNumber.text.toString()

        val verificationIntent = Intent(this, VerificationActivity::class.java)
        verificationIntent.putExtra(NUMBER_EXTRA, phoneNumber)
        startActivity(verificationIntent)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        editTextCountryCode.setText(countryCodesList.get(position))
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}