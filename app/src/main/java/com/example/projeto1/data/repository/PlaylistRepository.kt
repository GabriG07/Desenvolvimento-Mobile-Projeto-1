package com.example.projeto1.data.repository

import com.example.projeto1.data.Track
import com.example.projeto1.data.db.PlaylistDao
import com.example.projeto1.data.db.PlaylistEntity
import com.example.projeto1.data.db.TrackEntity
import com.example.projeto1.data.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

//Repositório para salvar as playlists localmente
class PlaylistRepository(private val dao: PlaylistDao) {

    fun observePlaylists(): Flow<List<PlaylistEntity>> = dao.observePlaylists()

    suspend fun getPlaylist(id: Long): PlaylistEntity? = dao.getPlaylist(id)

    suspend fun createPlaylist(name: String, coverUrl: String? = null): Long =
        dao.insertPlaylist(PlaylistEntity(name = name, coverUrl = coverUrl))

    suspend fun deletePlaylist(id: Long) = dao.deletePlaylist(id)

    //Marca a última playlist tocada, usado na tela de inicio
    suspend fun markPlaylistAsPlayed(id: Long) =
        dao.updateLastPlayed(id, System.currentTimeMillis())

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun observeLastPlayedWithCover(): Flow<Pair<PlaylistEntity, String?>?> =
        dao.observeLastPlayedPlaylist().flatMapLatest { playlist ->
            if (playlist == null) {
                flowOf(null)
            } else {
                dao.observeFirstTrackOf(playlist.id).map { firstTrack ->
                    playlist to firstTrack?.coverUrl
                }
            }
        }

    fun observeFirstCoverOf(playlistId: Long): Flow<String?> =
        dao.observeFirstTrackOf(playlistId).map { it?.coverUrl }

    fun observeTrackEntities(playlistId: Long): Flow<List<TrackEntity>> =
        dao.observeTracks(playlistId)

    suspend fun addTrack(playlistId: Long, track: Track) {
        val already = dao.getTracks(playlistId).any { it.deezerId == track.deezerId }
        if (!already) dao.insertTrack(track.toEntity(playlistId))
    }

    suspend fun removeTrack(rowId: Long) = dao.deleteTrack(rowId)
}