package com.example.projeto1.data.repository

import com.example.projeto1.data.Track
import com.example.projeto1.data.db.PlaylistDao
import com.example.projeto1.data.db.PlaylistEntity
import com.example.projeto1.data.db.TrackEntity
import com.example.projeto1.data.toDomain
import com.example.projeto1.data.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepository {
    class PlaylistRepository(private val dao: PlaylistDao) {

        fun observePlaylists(): Flow<List<PlaylistEntity>> = dao.observePlaylists()

        suspend fun getPlaylist(id: Long): PlaylistEntity? = dao.getPlaylist(id)

        suspend fun createPlaylist(name: String, coverUrl: String? = null): Long =
            dao.insertPlaylist(PlaylistEntity(name = name, coverUrl = coverUrl))

        suspend fun deletePlaylist(id: Long) = dao.deletePlaylist(id)

        fun observeTracks(playlistId: Long): Flow<List<Track>> =
            dao.observeTracks(playlistId).map { list -> list.map { it.toDomain() } }

        fun observeTrackEntities(playlistId: Long): Flow<List<TrackEntity>> =
            dao.observeTracks(playlistId)

        suspend fun getTrackEntities(playlistId: Long): List<TrackEntity> =
            dao.getTracks(playlistId)

        suspend fun addTrack(playlistId: Long, track: Track) {
            // Evita duplicatas
            val already = dao.getTracks(playlistId).any { it.deezerId == track.deezerId }
            if (!already) dao.insertTrack(track.toEntity(playlistId))
        }

        suspend fun removeTrack(rowId: Long) = dao.deleteTrack(rowId)
    }
}