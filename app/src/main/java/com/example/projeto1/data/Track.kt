package com.example.projeto1.data

data class Track(
    val deezerId: Long,
    val artist: String,
    val coverUrl: String?,
    val previewUrl: String?,
    val durationSec: Int
)

fun DeezerTrack.toDomain(): Track = Track(
    deezerId = id,
    title = title,
    artist = artist?.name ?: "",
    coverUrl = album?.coverBig ?: album?.coverMedium ?: artist?.pictureMedium,
    previewUrl = preview,
    durationSec = duration
)

fun TrackEntity.toDomain(): Track = Track(
    deezerId = deezerId,
    title = title,
    artist = artistName,
    coverUrl = coverUrl,
    previewUrl = previewUrl,
    durationSec = durationSec
)

fun Track.toEntity(playlistId: Long): TrackEntity = TrackEntity(
    playlistId = playlistId,
    deezerId = deezerId,
    title = title,
    artistName = artist,
    coverUrl = coverUrl,
    previewUrl = previewUrl,
    durationSec = durationSec
)