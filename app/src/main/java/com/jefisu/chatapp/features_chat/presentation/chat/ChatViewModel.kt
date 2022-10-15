package com.jefisu.chatapp.features_chat.presentation.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.chatapp.features_chat.domain.use_cases.ChatUseCases
import com.jefisu.chatapp.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs = savedStateHandle.navArgs<ChatNavArgs>()

    private val messages = savedStateHandle.getStateFlow("messages", navArgs.messages)
    private val messageText = savedStateHandle.getStateFlow("messageText", "")

    val state = combine(messages, messageText) { messages, messageText ->
        ChatState(
            messages = messages,
            ownerUsername = navArgs.ownerUsername,
            recipientUsername = navArgs.recipientUsername,
            recipientAvatarUrl = navArgs.recipientAvatarUrl,
            messageText = messageText
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ChatState())

    fun connectChat() {
        viewModelScope.launch {
            val connected =
                chatUseCases.connectToChat(navArgs.ownerUsername, navArgs.recipientUsername)
            if (connected) {
                chatUseCases.observeMessages().collect { message ->
                    val newList = messages.value.toMutableList().apply {
                        add(0, message)
                    }
                    savedStateHandle["messages"] = newList
                }
            }
        }
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.MessageTextChange -> {
                savedStateHandle["messageText"] = event.value
            }
            is ChatEvent.ClearText -> {
                savedStateHandle["messageText"] = ""
            }
            is ChatEvent.SendMessage -> sendMessage()
        }
    }

    fun exitChat() {
        viewModelScope.launch {
            chatUseCases.exitChat()
        }
    }

    private fun sendMessage() {
        viewModelScope.launch {
            if (messageText.value.isNotBlank()) {
                chatUseCases.sendMessage(messageText.value)
                savedStateHandle["messageText"] = ""
            }
        }
    }
}