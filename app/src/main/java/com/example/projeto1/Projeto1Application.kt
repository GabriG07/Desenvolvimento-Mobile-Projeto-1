package com.example.projeto1

import android.app.Application

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