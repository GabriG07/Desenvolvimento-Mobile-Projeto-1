package com.example.projeto1.data.repository

import com.example.projeto1.data.Track
import com.example.projeto1.data.api.DeezerAlbum
import com.example.projeto1.data.api.DeezerApi
import com.example.projeto1.data.api.DeezerArtist
import com.example.projeto1.data.api.DeezerSearchResponse
import com.example.projeto1.data.api.DeezerTrack
import com.example.projeto1.data.api.LyricsOvhApi
import com.example.projeto1.data.api.LyricsOvhResponse
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class MusicRepositoryTest {

    // Simulação da Api do Deezer, devolve sempre a mesma resposta
    private class FakeDeezerApi(private val resp: DeezerSearchResponse) : DeezerApi {
        override suspend fun search(query: String): DeezerSearchResponse = resp
    }

    // Simulação da LyricsOvhApi
    private class FakeLyricsOvhApi(
        private val responder: (artist: String, title: String) -> LyricsOvhResponse
    ) : LyricsOvhApi {
        override suspend fun getLyrics(artist: String, title: String): LyricsOvhResponse =
            responder(artist, title)
    }

    //Gera uma HttpException 404 igual à que o Retrofit lançaria
    private fun http404(): HttpException {
        val body = "{\"error\":\"No lyrics found\"}"
            .toResponseBody("application/json".toMediaType())
        return HttpException(Response.error<Any>(404, body))
    }

    private fun newRepo(
        deezerResp: DeezerSearchResponse = DeezerSearchResponse(emptyList(), 0),
        lyricsResponder: (String, String) -> LyricsOvhResponse = { _, _ ->
            LyricsOvhResponse(lyrics = null, error = "not used")
        }
    ) = MusicRepository(
        deezerApi = FakeDeezerApi(deezerResp),
        lyricsOvhApi = FakeLyricsOvhApi(lyricsResponder)
    )

    private val sampleTrack = Track(
        deezerId = 1L,
        title = "Scar Tissue",
        artist = "Red Hot Chili Peppers",
        coverUrl = null,
        previewUrl = null,
        durationSec = 180
    )


    /** Search Tracks - Deezer Api */
    @Test
    fun searchTracks_queryVazia_retornaListaVazia() = runBlocking {
        val repo = newRepo(DeezerSearchResponse(emptyList(), 0))

        val result = repo.searchTracks("   ")

        assertTrue(result.isEmpty())
    }

    @Test
    fun searchTracks_devolveMusicasMapeadas() = runBlocking {
        val deezerResp = DeezerSearchResponse(
            data = listOf(
                DeezerTrack(
                    id = 1L,
                    title = "Scar Tissue",
                    preview = "preview1.mp3",
                    duration = 200,
                    artist = DeezerArtist(1, "Red Hot Chilli Peppers", null),
                    album = DeezerAlbum(1, "Californication", "med.jpg", "big.jpg")
                ),
                DeezerTrack(
                    id = 2L,
                    title = "My Sacrifice",
                    preview = "preview2.mp3",
                    duration = 210,
                    artist = DeezerArtist(2, "Creed", null),
                    album = DeezerAlbum(2, "Weathered", null, null)
                )
            ),
            total = 2
        )
        val repo = newRepo(deezerResp)

        val result = repo.searchTracks("pop")

        assertEquals(2, result.size)
        assertEquals("Scar Tissue", result[0].title)
        assertEquals("Red Hot Chilli Peppers", result[0].artist)
        assertEquals("big.jpg", result[0].coverUrl) // prefere coverBig
        assertEquals("My Sacrifice", result[1].title)
        assertEquals("Creed", result[1].artist)
    }

    /** Search lyrics - LyricsOvhApi*/
    @Test
    fun fetchLyrics_quandoLyricsOvhRetornaLetra_devolveTexto() = runBlocking {
        val repo = newRepo(lyricsResponder = { artist, title ->
            // Verifica que o repository passa artist/title corretos para a API
            assertEquals("Red Hot Chili Peppers", artist)
            assertEquals("Scar Tissue", title)
            LyricsOvhResponse(lyrics = "With the birds I'll share this lonely view...")
        })

        val lyrics = repo.fetchLyrics(sampleTrack)

        assertEquals("With the birds I'll share this lonely view...", lyrics)
    }

    @Test
    fun fetchLyrics_quandoCampoLetraVemVazio_devolveNull() = runBlocking {
        val repo = newRepo(lyricsResponder = { _, _ ->
            LyricsOvhResponse(lyrics = "   ")
        })

        val lyrics = repo.fetchLyrics(sampleTrack)

        assertNull(lyrics)
    }

    @Test(expected = LyricsException.HttpError::class)
    fun fetchLyrics_quandoServidorRetornaErroNaoEsperado_lancaExcecao() = runBlocking {
        // código que não seja 404 deve ser lançado como LyricsException.HttpError para a UI mostrar a mensagem de erro.
        val repo = newRepo(lyricsResponder = { _, _ ->
            val body = "".toResponseBody("application/json".toMediaType())
            throw HttpException(Response.error<Any>(500, body))
        })

        repo.fetchLyrics(sampleTrack)
        Unit
    }
}