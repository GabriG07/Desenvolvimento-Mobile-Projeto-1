package com.example.projeto1.data

import com.google.gson.Gson

data class UserModel(val userName: String = "", val mobileNumber: String = "", var contacts: List<String> = listOf<String>()) {
    fun addContact(newContact: String) {
        contacts = contacts + newContact
    }
}