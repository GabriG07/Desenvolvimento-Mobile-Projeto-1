package com.example.projeto1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projeto1.Projeto1Application
import com.example.projeto1.data.Contact
import com.example.projeto1.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Responsável por guardar o estado da busca de músicas e das letras.
 * O player de áudio é exposto diretamente para que as telas possam observar`currentTrack`, `isPlaying` e `progress`.
 */
class ChatViewModel(
        private val contact: Contact,
    ) : ViewModel() {

    val _contato = MutableStateFlow<Contact>(contact)
    val contato = _contato.asStateFlow()

    fun setContato(novoContato: Contact) {
        _contato.value = novoContato
    }


    //Factory usando os singletons definidos na Application.
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = Projeto1Application.instance
                return ChatViewModel(app.contact) as T
            }
        }
    }
}