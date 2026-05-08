package com.emirsoylemez.hukukasistan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChatViewModel : ViewModel() {
    // Mesaj listesini burada saklıyoruz
    private val _messages = MutableLiveData<MutableList<ChatMessage>>(mutableListOf())
    val messages: LiveData<MutableList<ChatMessage>> get() = _messages

    fun addMessage(message: ChatMessage) {
        val currentList = _messages.value ?: mutableListOf()
        currentList.add(message)
        _messages.value = currentList // Observer'ı tetiklemek için listeyi tekrar set ediyoruz
    }
}