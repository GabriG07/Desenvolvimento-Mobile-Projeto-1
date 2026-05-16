package com.example.projeto1.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa uma playlist criada pelo usuário. Armazenada localmente com Room
 */

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val coverUrl: String? = null,
    val lastPlayedAt: Long? = null
)
