package com.imran.android.kotlinphonenumberauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

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

                verificationInProgress = false
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

            }
        }

        phoneNumber = intent.getStringExtra(NUMBER_EXTRA).toString()
        textNumberInVerification.text = phoneNumber

        if (!verificationInProgress) {
            startPhoneNumberVerification(phoneNumber)
        }
    }

    // Get the text code sent so user can use it for sign in
    private fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(onVerificationStateChangedCallbacks)
                .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
        verificationInProgress = true
    }

    // Use text to sign in
    private fun signInWithPhoneAuthCredential(phoneAuthCredential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithCredential:success")

                        val signedInIntent = Intent(this, SignedInActivity::class.java)
                        signedInIntent.putExtra(NUMBER_EXTRA, phoneNumber)
                        startActivity(signedInIntent)
                    } else {
                        // Sign in failed, display a message and update the UI
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            editTextCode.setError("Invalid Code")
                        }
                    }
                }
    }

    // entered code to manually for log in (code from text)
    private fun verifyPhoneNumberWithCode(verificationId: String, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
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
        val code: String =  editTextCode.text.toString()
        if (code.isEmpty()) {
            editTextCode.setError("Can not be empty")
        } else {
            verifyPhoneNumberWithCode(currentVerificationId, code)
        }
    }
}