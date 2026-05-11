package com.example.projeto1.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
* singletons das APIs
*
* Constrói as instâncias de Retrofit para cada API
*/

object NetworkModule {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val deezerApi: DeezerApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.deezer.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DeezerApi::class.java)
    }

    val lyricsOvhApi: LyricsOvhApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.lyrics.ovh/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LyricsOvhApi::class.java)
    }
}