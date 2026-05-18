package com.example.projeto1.ui.viewmodel

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projeto1.data.UserModel
import com.example.projeto1.ui.OnBoardingEvents
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class OnboardingViewModel : ViewModel(){

    val databaseRef = FirebaseDatabase.getInstance()
    val userDb = databaseRef.getReference("Users")
    var loggedUser: UserModel = UserModel()

    lateinit var activity: Activity

    fun provideActivity(activity: Activity) {
        this.activity = activity
    }

    var userModel = UserModel()

    fun action(even: OnBoardingEvents){
        when (even) {
            is OnBoardingEvents.SignUpClick -> signUpClick(even.userModel, even.status)
            is OnBoardingEvents.LoginClick -> loginClick(even.userModel, even.sharedPreferences, even.status)

        }
    }

    private fun loginClick(userModel: UserModel, sharedPreferences: SharedPreferences?, status: (status: Boolean) -> Unit) {
        val checkUser: Query = userDb.orderByChild("mobileNumber").equalTo(userModel.mobileNumber)

        checkUser.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    val userName = snapshot.child(userModel.mobileNumber).child("userName").getValue(String::class.java)
                    val phoneNumber = snapshot.child(userModel.mobileNumber).child("mobileNumber").getValue(String::class.java)
                    loggedUser = loggedUser.copy(userName = userName!!, mobileNumber = phoneNumber!!)
                    sharedPreferences?.edit()?.putString("mobileNumber", phoneNumber)?.apply()
                    status(true)

                } else {
                    status(false)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                status(false)
            }
        })
    }

    private fun signUpClick(userModel: UserModel, status: (status: Boolean) -> Unit) {
        this.userModel = userModel
        val id = userDb.push().key!!
        userDb.child(userModel.mobileNumber).setValue(userModel)

    }

//    private fun verifyOtp(credential: PhoneAuthCredential) {
//        try {
//            viewModelScope.launch {
//                    auth.signInWithCredential(credential).addOnCompleteListener {
//                        task -> if (task.isSuccessful) {
//                            viewModelScope.launch {
//                                val key = databaseRef.getReference("users").push().key
//                                key?.let {
//                                    databaseRef.getReference("users").child(it).setValue(userModel)
//                                        .addOnSuccessListener {
//
//                                        }
//                                        .addOnFailureListener {
//
//                                        }
//                                }
//                            }
//
//                    } else {
//
//                    }
//                }
//            }
//        } catch (e: Exception) {
//
//        }
//
//    }


    override fun onCleared() {
        super.onCleared()
    }
}