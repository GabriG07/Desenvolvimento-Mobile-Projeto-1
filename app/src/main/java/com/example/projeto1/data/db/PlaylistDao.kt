package com.example.projeto1.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    //Playlists
    @Query("SELECT * FROM playlists ORDER BY id DESC")
    fun observePlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE id = :id")
    suspend fun getPlaylist(id: Long): PlaylistEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Query("DELETE FROM playlists WHERE id = :id")
    suspend fun deletePlaylist(id: Long)

    // Músicas de uma playlist
    @Query("SELECT * FROM playlist_tracks WHERE playlistId = :playlistId ORDER BY rowId ASC")
    fun observeTracks(playlistId: Long): Flow<List<TrackEntity>>

    @Query("SELECT * FROM playlist_tracks WHERE playlistId = :playlistId ORDER BY rowId ASC")
    suspend fun getTracks(playlistId: Long): List<TrackEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity): Long

    @Query("DELETE FROM playlist_tracks WHERE rowId = :rowId")
    suspend fun deleteTrack(rowId: Long)
}