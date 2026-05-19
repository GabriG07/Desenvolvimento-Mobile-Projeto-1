package com.example.projeto1.data.repository

import android.util.Log
import com.example.projeto1.data.Track
import com.example.projeto1.data.api.DeezerApi
import com.example.projeto1.data.api.LyricsOvhApi
import com.example.projeto1.data.toDomain
import retrofit2.HttpException

/**
 * Repository responsável pelas operações de música feitas pela internet.
 *
 * Duas APIs web são usadas:
 *  - Deezer (busca + preview MP3 de 30 segundos)
 *  - lyrics.ovh (letras de músicas)
 */
class MusicRepository(
    private val deezerApi: DeezerApi,
    private val lyricsOvhApi: LyricsOvhApi,
) {

    // Busca músicas no Deezer
    suspend fun searchTracks(query: String): List<Track> {
        if (query.isBlank()) return emptyList()
        val response = deezerApi.search(query)
        return response.data.map { it.toDomain() }
    }


    //Busca a letra
    suspend fun fetchLyrics(track: Track): String? {
        val viaLyricsOvh = fetchFromLyricsOvh(track)
        if (!viaLyricsOvh.isNullOrBlank()) return viaLyricsOvh

        return null
    }

    // lyrics.ovh
    private suspend fun fetchFromLyricsOvh(track: Track): String? {
        return try {
//            Log.d("MusicRepository", "lyrics.ovh -> ${track.artist} / ${track.title}")

            val resp = lyricsOvhApi.getLyrics(
                artist = track.artist.trim(),
                title = track.title.trim()
            )

            val text = resp.lyrics?.trim()

            if (text.isNullOrBlank()) {
//                Log.d("MusicRepository", "lyrics.ovh: campo de letra vazio")
                null
            } else {
//                Log.d("MusicRepository", "lyrics.ovh: encontrou ${text.length} caracteres")
                text
            }

        } catch (e: HttpException) {
            // 404 significa que a música não foi encontrada no lyrics.ovh.
            if (e.code() == 404) {
//                Log.d("MusicRepository", "lyrics.ovh: 404 (não encontrada)")
                null
            } else {
//                Log.e("MusicRepository", "lyrics.ovh HTTP ${e.code()}", e)
                throw LyricsException.HttpError(e.code())
            }

        } catch (e: Throwable) {
//            Log.e("MusicRepository", "falha no lyrics.ovh", e)
            throw LyricsException.Network(e.message ?: "erro de rede")
        }
    }
}

/**
 * Representa os diferentes tipos de erro que podem acontecer ao buscar letras.
 */
sealed class LyricsException(message: String) : Exception(message) {
    data class HttpError(val code: Int) : LyricsException("Serviço de letras retornou HTTP $code")
    data class Network(val detail: String) : LyricsException("Erro de rede: $detail")
}