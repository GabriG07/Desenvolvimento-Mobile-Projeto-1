package com.example.projeto1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projeto1.Projeto1Application
import com.example.projeto1.data.AudioPlayer
import com.example.projeto1.data.Track
import com.example.projeto1.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Responsável por guardar o estado da busca de músicas e das letras.
 * O player de áudio é exposto diretamente para que as telas possam observar`currentTrack`, `isPlaying` e `progress`.
 */
class MusicViewModel(
    private val repository: MusicRepository,
    val player: AudioPlayer
) : ViewModel() {

    // Estado da busca
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _searchUi = MutableStateFlow(SearchUi())
    val searchUi: StateFlow<SearchUi> = _searchUi.asStateFlow()

    fun onQueryChanged(value: String) {
        _query.value = value
    }

    fun search() {
        val q = _query.value.trim()
        if (q.isEmpty()) {
            _searchUi.value = SearchUi(results = emptyList())
            return
        }
        _searchUi.value = SearchUi(loading = true)
        viewModelScope.launch {
            try {
                val tracks = repository.searchTracks(q)
                _searchUi.value = SearchUi(loading = false, results = tracks)
            } catch (t: Throwable) {
                _searchUi.value = SearchUi(loading = false, error = true)
            }
        }
    }

    // Controles do player
    fun playTrack(track: Track) = player.play(track)
    fun playFromList(tracks: List<Track>, index: Int) = player.playFromQueue(tracks, index)
    fun next() = player.playNext()
    fun previous() = player.playPrevious()
    fun togglePlay() = player.toggle()

    // Letras das músicas
    private val _lyricsUi = MutableStateFlow(LyricsUi())
    val lyricsUi: StateFlow<LyricsUi> = _lyricsUi.asStateFlow()

    fun loadLyricsForCurrentTrack() {
        val track = player.currentTrack.value ?: return
        _lyricsUi.value = LyricsUi(loading = true)
        viewModelScope.launch {
            try {
                val lyrics = repository.fetchLyrics(track)
                _lyricsUi.value = if (lyrics.isNullOrBlank())
                    LyricsUi(loading = false, notFound = true)
                else
                    LyricsUi(loading = false, lyrics = lyrics)
            } catch (t: Throwable) {
                _lyricsUi.value = LyricsUi(
                    loading = false,
                    error = true,
                    errorMessage = t.message
                )
            }
        }
    }

    data class SearchUi(
        val loading: Boolean = false,
        val results: List<Track> = emptyList(),
        val error: Boolean = false
    )

    data class LyricsUi(
        val loading: Boolean = false,
        val lyrics: String? = null,
        val notFound: Boolean = false,
        val error: Boolean = false,
        val errorMessage: String? = null
    )

    //Factory usando os singletons definidos na Application.
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = Projeto1Application.instance
                return MusicViewModel(app.musicRepository, app.audioPlayer) as T
            }
        }
    }
}