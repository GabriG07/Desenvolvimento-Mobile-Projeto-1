package com.example.projeto1.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime


@RequiresApi(Build.VERSION_CODES.O)
val pedroLastMessage = Message(
    message = "Que horas sai o album?",
    time = LocalDateTime.now(),
    sender = "1"
)
@RequiresApi(Build.VERSION_CODES.O)
val contato2LastMessage = Message(
    message = "Vai viajar amanha?",
    time = LocalDateTime.now(),
    sender = "1"
)
@RequiresApi(Build.VERSION_CODES.O)
val contato3LastMessage = Message(
    message = "Se liga nesse som",
    time = LocalDateTime.now(),
    sender = "1"
)
@RequiresApi(Build.VERSION_CODES.O)
val contato = Contact(
    name = "Carlos",
    lastMessage = pedroLastMessage,
    profilePicture = "https://img.magnific.com/fotos-gratis/retrato-de-homem-branco-isolado_53876-40306.jpg?semt=ais_hybrid&w=740&q=80",
    id = "1"
)

@RequiresApi(Build.VERSION_CODES.O)
val contato2 = Contact(
    name = "Pedro Alberto",
    lastMessage = contato2LastMessage,
    profilePicture = "https://images.unsplash.com/photo-1564564321837-a57b7070ac4f?fm=jpg&q=60&w=3000&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTB8fG1hc2N1bGlub3xlbnwwfHwwfHx8MA%3D%3D",
    id = "2"
)

@RequiresApi(Build.VERSION_CODES.O)
val contato3 = Contact(
    name = "Jose Maia",
    lastMessage = contato3LastMessage,
    profilePicture = "https://img.freepik.com/fotos-gratis/jovem-barbudo-satisfeito-de-camisa-preta-olhando-para-a-camera-com-um-sorriso-confiante-no-rosto_141793-28112.jpg?semt=ais_hybrid&w=740&q=80",
    id = "3"
)

val contatos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    listOf(
        contato,
        contato2,
        contato3
    )
} else {
    TODO("VERSION.SDK_INT < O")
}