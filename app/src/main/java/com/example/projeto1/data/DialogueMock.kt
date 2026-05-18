package com.example.projeto1.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
val dialogo = listOf(
    Message(
        message = "Fala cara, tudo bom?",
        time = LocalDateTime.now(),
        sender = "1"
    ),
    Message(
        message = "Fala meu amigo, tudo bom aqui, e ai?",
        time = LocalDateTime.now(),
        sender = "2"
    ),
    Message(
        message = "Tudo bom aqui tambem. Aqui, Posso te perguntar uma coisa?",
        time = LocalDateTime.now(),
        sender = "1"
    ),
    Message(
        message = "Tudo bem se nao quiser responder, mas eh rapidinho",
        time = LocalDateTime.now(),
        sender = "1"
    ),
    Message(
        message = "Claro man, fala ai?",
        time = LocalDateTime.now(),
        sender = "2"
    ),
    Message(
        message = "Qual sua musica favorita do Michael Jackson?",
        time = LocalDateTime.now(),
        sender = "1"
    ),
    Message(
        message = "Sei la vei kkkkkkkkkkkkkk",
        time = LocalDateTime.now(),
        sender = "2"
    ),
    Message(
        message = "Porque quer saber?",
        time = LocalDateTime.now(),
        sender = "2"
    ),
    Message(
        message = "To fazendo um trabalho de faculdade",
        time = LocalDateTime.now(),
        sender = "1"
    ),
    Message(
        message = "Beleza, eh beat it",
        time = LocalDateTime.now(),
        sender = "2"
    ),

)