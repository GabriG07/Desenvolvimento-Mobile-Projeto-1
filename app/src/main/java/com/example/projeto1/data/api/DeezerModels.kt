package com.example.projeto1.data.api

import com.google.gson.annotations.SerializedName


//Endpoint: https://api.deezer.com/search?q=<query>
data class DeezerSearchResponse(
    @SerializedName("data") val data: List<DeezerTrack> = emptyList(),
    @SerializedName("total") val total: Int = 0
)

data class DeezerTrack(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("preview") val preview: String?,
    @SerializedName("duration") val duration: Int,
    @SerializedName("artist") val artist: DeezerArtist?,
    @SerializedName("album") val album: DeezerAlbum?
)

data class DeezerArtist(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("picture_medium") val pictureMedium: String?
)

data class DeezerAlbum(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("cover_medium") val coverMedium: String?,
    @SerializedName("cover_big") val coverBig: String?
)