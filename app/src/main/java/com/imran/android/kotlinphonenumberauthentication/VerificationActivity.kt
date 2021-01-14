package com.imran.android.kotlinphonenumberauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class VerificationActivity : AppCompatActivity() {
    private val NUMBER_EXTRA = "NUMBER_EXTRA"
    private val TAG = "PhoneAuthActivity"
    private val KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress"
    private var phoneNumber = ""

    private lateinit var textNumberInVerification: TextView
    private lateinit var editTextCode: EditText

    private var verificationInProgress = false
    private var verificationCompleted = false
    private var currentVerificationId: String = ""
    private lateinit var currentForceResendingToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var onVerificationStateChangedCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        // restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        }

        textNumberInVerification = findViewById(R.id.textNumberVerification)
        editTextCode = findViewById(R.id.editTextCode)
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize phone auth callbacks
        onVerificationStateChangedCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$phoneAuthCredential")

                signInWithPhoneAuthCredential(phoneAuthCredential)
            }

            override fun onVerificationFailed(firebaseException: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                        Log.w(TAG, "onVerificationFailed", firebaseException)

                if (firebaseException is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (firebaseException is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                currentVerificationId = verificationId
                currentForceResendingToken = token

                // ...
            }
        }

//        phoneNumber =
    }

    private fun signInWithPhoneAuthCredential(phoneAuthCredential: PhoneAuthCredential) {

    }

    public fun actionWrongNumber(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, verificationInProgress)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        verificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS)
    }

    public fun actionSubmit(view: View) {
        val signedInIntent = Intent(this, SignedInActivity::class.java)
//        signedInIntent.putExtra(NUMBER_EXTRA, phoneNumber)
        startActivity(signedInIntent)
    }
}