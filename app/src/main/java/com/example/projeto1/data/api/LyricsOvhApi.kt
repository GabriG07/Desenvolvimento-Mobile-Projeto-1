package com.example.projeto1.data.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * API — https://lyrics.ovh
 * Endpoint: GET https://api.lyrics.ovh/v1/{artist}/{title}
 *
 * Retorno:
 *   200 OK   -> { "lyrics": "Verso 1...\n..." }
 *   404      -> { "error": "No lyrics found" }
 */
interface LyricsOvhApi {

    @GET("v1/{artist}/{title}")
    suspend fun getLyrics(
        @Path("artist") artist: String,
        @Path("title") title: String
    ): LyricsOvhResponse
}

data class LyricsOvhResponse(
    @SerializedName("lyrics") val lyrics: String? = null,
    @SerializedName("error") val error: String? = null
)