package com.example.projeto1.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * Representa uma música salva em uma playlist.
 * Cada música em uma playlist é uma row em playlist_tracks.
 * A foreign key com onDelete = CASCADE garante que apagar uma playlist apaga suas músicas automaticamente.
 * */
@Entity(
    tableName = "playlist_tracks",
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("playlistId")]
)
data class TrackEntity(
    @PrimaryKey(autoGenerate = true) val rowId: Long = 0,
    val playlistId: Long,
    val deezerId: Long,
    val title: String,
    val artistName: String,
    val coverUrl: String?,
    val previewUrl: String?,
    val durationSec: Int
)