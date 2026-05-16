package com.example.projeto1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projeto1.Projeto1Application
import com.example.projeto1.data.Track
import com.example.projeto1.data.db.PlaylistEntity
import com.example.projeto1.data.repository.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class PlaylistViewModel(
    private val repository: PlaylistRepository
) : ViewModel() {

    val playlists: StateFlow<List<PlaylistEntity>> =
        repository.observePlaylists()
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val lastPlayedWithCover: StateFlow<Pair<PlaylistEntity, String?>?> =
        repository.observeLastPlayedWithCover()
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun markPlaylistAsPlayed(id: Long) {
        viewModelScope.launch { repository.markPlaylistAsPlayed(id) }
    }

    fun createPlaylist(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch { repository.createPlaylist(name.trim()) }
    }

    fun deletePlaylist(id: Long) {
        viewModelScope.launch { repository.deletePlaylist(id) }
    }

    fun addTrackToPlaylist(playlistId: Long, track: Track) {
        viewModelScope.launch { repository.addTrack(playlistId, track) }
    }

    fun removeTrack(rowId: Long) {
        viewModelScope.launch { repository.removeTrack(rowId) }
    }


    // URL da capa da primeira música de uma playlist
    fun firstCoverOf(playlistId: Long): Flow<String?> =
        repository.observeFirstCoverOf(playlistId)


    private val _detailName = MutableStateFlow<String?>(null)
    val detailName: StateFlow<String?> = _detailName.asStateFlow()

    fun loadDetail(playlistId: Long): Flow<List<com.example.projeto1.data.db.TrackEntity>> {
        viewModelScope.launch {
            _detailName.value = repository.getPlaylist(playlistId)?.name
        }
        return repository.observeTrackEntities(playlistId)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                PlaylistViewModel(Projeto1Application.instance.playlistRepository) as T
        }
    }
}