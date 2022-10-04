package com.jefisu.chatapp.features_chat.presentation.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.chatapp.features_chat.domain.use_cases.ChatUseCases
import com.jefisu.chatapp.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(ChatState())
        private set

    private val navArgs = savedStateHandle.navArgs<ChatNavArgs>()

    fun getChat() {
        navArgs.chatId?.let { id ->
            chatUseCases.getChat(id)
                .onEach { currentChat ->
                    state = state.copy(
                        messages = currentChat.messages,
                        ownerUsername = navArgs.ownerUsername,
                        recipientUser = currentChat.users.find { it.username != navArgs.ownerUsername }
                    )
                }.launchIn(viewModelScope)
        }
    }

    fun connectChat() {
        viewModelScope.launch {
            val connected =
                chatUseCases.connectToChat(navArgs.ownerUsername, navArgs.recipientUsername)
            if (connected) {
                chatUseCases.observeMessages().collect { message ->
                    val newList = state.messages.toMutableList().apply {
                        add(0, message)
                    }
                    state = state.copy(messages = newList)
                }
            }
        }
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.MessageTextChange -> {
                state = state.copy(messageText = event.value)
            }
            is ChatEvent.ClearText -> {
                state = state.copy(messageText = "")
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
            if (state.messageText.isNotBlank()) {
                chatUseCases.sendMessage(state.messageText)
                state = state.copy(messageText = "")
            }
        }
    }
}