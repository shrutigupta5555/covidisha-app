/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.covidishaa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.covidishaa.extensions.Extensions.toast
import com.example.covidishaa.utils.FirebaseUtils
import com.example.covidishaa.utils.FirebaseUtils.firebaseAuth
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import kotlinx.android.synthetic.main.activity_create_account.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {



    var number : String =""

    // create instance of firebase auth
    lateinit var auth: FirebaseAuth

    // we will use this to match the sent otp from firebase
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        Log.i("kk", "main ${FirebaseUtils.firebaseAuth.currentUser.toString()}")
        auth=FirebaseAuth.getInstance()

        // start verification on click of the button
        btnCreateAccount.setOnClickListener {
            login()
        }

        btnResendOTP.setOnClickListener{
            resendOTP()
        }

        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            // This method is called when the verification is completed
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(applicationContext, BottomNavActivity::class.java))
                finish()
                Log.d("GFG", "onVerificationCompleted Success")
            }

            // Called when verification is failed add log statement to see the exception
            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("GFG", "onVerificationFailed  $e")
            }

            // On code is sent by the firebase this method is called
            // in here we start a new activity where user can enter the OTP
            override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("GFG", "onCodeSent: $verificationId")
                storedVerificationId = verificationId
                resendToken = token

                number = etMobile.text.trim().toString()

                // get the phone number from edit text and append the country cde with it

                //validate data

                // Start a new activity using intent
                // also send the storedVerificationId using intent
                // we will use this id to send the otp back to firebase
                val intent = Intent(applicationContext, OtpActivity::class.java)
                intent.putExtra("storedVerificationId", storedVerificationId)
                intent.putExtra("phone", number)
                startActivity(intent)
                finish()
            }
        }

    }


    /* check if there's a signed-in user*/
    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = firebaseAuth.currentUser
        user?.let {
            startActivity(Intent(this, BottomNavActivity::class.java))
            toast("welcome back")
        }

    }

    private fun notEmpty(): Boolean =
            etMobile.text.toString().trim().isNotEmpty()


    private fun resendOTP(){
        number = etMobile.text.trim().toString()

        // get the phone number from edit text and append the country cde with it
        if (number.isNotEmpty()){
            number = "+91$number"
            resendVerificationCode(number)
        }else{
            Toast.makeText(this, "Enter mobile number", Toast.LENGTH_SHORT).show()
        }
    }

    private fun login() {
        number = etMobile.text.trim().toString()

        // get the phone number from edit text and append the country cde with it
        if (number.isNotEmpty() && number.length == 10){

            number = "+91$number"
            sendVerificationCode(number)
        }else{

            etMobile.error = "Enter valid phone number"
//            Toast.makeText(this, "Enter valid phone number", Toast.LENGTH_SHORT).show()
        }
    }

    // this method sends the verification code
    // and starts the callback of verification
    // which is implemented above in onCreate
    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(number) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d("GFG", "Auth started")
    }

    private fun resendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(number) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .setForceResendingToken(resendToken!!) // ForceResendingToken from callbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}


