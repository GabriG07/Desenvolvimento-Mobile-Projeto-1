package com.example.projeto1.data.api

import retrofit2.http.GET
import retrofit2.http.Query


// Base URL: https://api.deezer.com/
interface DeezerApi {
    @GET("search")
    suspend fun search(@Query("q") query: String): DeezerSearchResponse
}