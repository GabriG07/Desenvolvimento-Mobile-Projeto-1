package com.example.projeto1.data

import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * A API gratuita do Deezer retorna URLs de previews MP3 de 30 segundos. Nós fazemos o streaming desses previews.
 * O player expõe seu estado atual como Flows para que a UI possa observar.
 */
class AudioPlayer {

    private var player: MediaPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack: StateFlow<Track?> = _currentTrack

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _progress = MutableStateFlow(0f) //Varia de 0f(início da música) até 1f(fim da música)
    val progress: StateFlow<Float> = _progress

    // A fila que estamos reproduzindo. Fica vazia quando nada foi carregado.
    private val _queue = MutableStateFlow<List<Track>>(emptyList())
    val queue: StateFlow<List<Track>> = _queue

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private var progressJob: kotlinx.coroutines.Job? = null

    //Toca uma única música
    fun play(track: Track) {
        playFromQueue(listOf(track), 0)
    }


    /*
      Reproduz a música no [startIndex] dentro da lista fornecida. Próxima/Anterior vão
      navegar dentro dessa lista. Essa é a forma preferida de iniciar a reprodução
      pela UI: passe a busca/playlist inteira e o índice do item tocado.
     */
    fun playFromQueue(tracks: List<Track>, startIndex: Int) {
        if (tracks.isEmpty()) return
        val safeIndex = startIndex.coerceIn(0, tracks.size - 1)
        _queue.value = tracks
        _currentIndex.value = safeIndex
        startTrack(tracks[safeIndex])
    }

    // Pula para a próxima música da fila (volta para o início ao chegar no fim).
    fun playNext() {
        val q = _queue.value
        if (q.isEmpty()) return
        val newIndex = (_currentIndex.value + 1) % q.size
        _currentIndex.value = newIndex
        startTrack(q[newIndex])
    }

    // Pula para a música anterior da fila (volta para o fim ao chegar no início).
    fun playPrevious() {
        val q = _queue.value
        if (q.isEmpty()) return
        val newIndex = if (_currentIndex.value - 1 < 0) q.size - 1 else _currentIndex.value - 1
        _currentIndex.value = newIndex
        startTrack(q[newIndex])
    }

    /*
     Carrega e inicia uma música usando o MediaPlayer.
     Se a mesma música já estiver carregada, apenas retomamos a reprodução.
     */
    private fun startTrack(track: Track) {
        if (_currentTrack.value?.deezerId == track.deezerId && player != null) {
            player?.start()
            _isPlaying.value = true
            startProgressTicker()
            return
        }

        release()
        val url = track.previewUrl ?: return  // nada para reproduzir
        _currentTrack.value = track
        _progress.value = 0f

        player = MediaPlayer().apply {
            setDataSource(url)
            setOnPreparedListener {
                start()
                _isPlaying.value = true
                startProgressTicker()
            }
            setOnCompletionListener {
                _isPlaying.value = false
                _progress.value = 1f
                // Avança automaticamente para a próxima música quando o preview termina.
                playNext()
            }
            setOnErrorListener { _, _, _ ->
                _isPlaying.value = false
                true
            }
            prepareAsync()
        }
    }

    fun toggle() {
        val mp = player ?: return
        if (mp.isPlaying) {
            mp.pause()
            _isPlaying.value = false
        } else {
            mp.start()
            _isPlaying.value = true
            startProgressTicker()
        }
    }

    fun stop() {
        release()
        _isPlaying.value = false
        _progress.value = 0f
    }

    private fun startProgressTicker() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (isActive) {
                val mp = player ?: break
                val total = mp.duration.takeIf { it > 0 } ?: break
                _progress.value = mp.currentPosition.toFloat() / total.toFloat()
                delay(250)
            }
        }
    }

    private fun release() {
        progressJob?.cancel()
        progressJob = null
        try {
            player?.stop()
        } catch (_: IllegalStateException) {
            // nao faz nada
        }
        player?.release()
        player = null
    }
}