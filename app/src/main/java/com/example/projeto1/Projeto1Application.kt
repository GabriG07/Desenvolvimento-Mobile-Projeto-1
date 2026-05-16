package com.example.projeto1

import android.app.Application
import com.example.projeto1.data.AudioPlayer
import com.example.projeto1.data.api.NetworkModule
import com.example.projeto1.data.db.AppDatabase
import com.example.projeto1.data.repository.MusicRepository
import com.example.projeto1.data.repository.PlaylistRepository

class Projeto1Application : Application() {

    lateinit var musicRepository: MusicRepository private set
    lateinit var playlistRepository: PlaylistRepository private set
    lateinit var audioPlayer: AudioPlayer private set

    override fun onCreate() {
        super.onCreate()
        instance = this;

        val db = AppDatabase.get(this)
        playlistRepository = PlaylistRepository(db.playlistDao())
        musicRepository = MusicRepository(
            deezerApi = NetworkModule.deezerApi,
            lyricsOvhApi = NetworkModule.lyricsOvhApi,
        )
        audioPlayer = AudioPlayer()
    }

    companion object { //semelhante ao 'static' em java
        lateinit var instance: Projeto1Application private set
    }

}