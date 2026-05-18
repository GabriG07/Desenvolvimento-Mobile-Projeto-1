package com.example.projeto1.ui

import android.content.SharedPreferences
import com.example.projeto1.data.UserModel

sealed interface OnBoardingEvents {
    data class SignUpClick(val userModel: UserModel, val status :( status: Boolean) -> Unit) : OnBoardingEvents
    data class LoginClick(val userModel: UserModel, val sharedPreferences: SharedPreferences?, val status :(status: Boolean) -> Unit) : OnBoardingEvents
}