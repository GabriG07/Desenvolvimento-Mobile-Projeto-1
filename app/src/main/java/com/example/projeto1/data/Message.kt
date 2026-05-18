package com.example.projeto1.data

import java.time.LocalDateTime

data class Message(
    val message: String,
    val time: LocalDateTime,
    val sender: String,
)