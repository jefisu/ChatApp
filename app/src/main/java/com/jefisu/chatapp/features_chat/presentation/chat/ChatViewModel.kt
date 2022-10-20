package com.jefisu.chatapp.features_chat.presentation.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.chatapp.core.util.Resource
import com.jefisu.chatapp.features_chat.data.dto.DeleteResource
import com.jefisu.chatapp.features_chat.domain.model.Message
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
    private val selectedMessages =
        savedStateHandle.getStateFlow("selectedMessages", emptyList<Message>())

    val state = combine(
        messages,
        messageText,
        selectedMessages
    ) { messages, messageText, selectedMessages ->
        ChatState(
            chatId = navArgs.chatId,
            messages = messages,
            ownerId = navArgs.ownerId,
            recipientUser = navArgs.recipientUser,
            messageText = messageText,
            selectedMessages = selectedMessages
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ChatState())

    fun connectChat() {
        viewModelScope.launch {
            val connected =
                chatUseCases.connectToChat(navArgs.ownerId, navArgs.recipientUser!!.id)
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
            is ChatEvent.SelectMessage -> selectedMessage(event.message)
            is ChatEvent.ClearSelectionMessages -> clearSelection()
            is ChatEvent.DeleteMessages -> deleteMessage()
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

    private fun deleteMessage() {
        viewModelScope.launch {
            if (selectedMessages.value.isEmpty()) {
                return@launch
            }
            val chatId = state.value.chatId ?: return@launch
            val deleteResource = DeleteResource(
                id = chatId,
                items = messages.value.map { it.id }
            )
            val result = chatUseCases.deleteMessage(deleteResource)
            if (result is Resource.Success) {
                savedStateHandle["messages"] =
                    messages.value.filter { !selectedMessages.value.contains(it) }
                savedStateHandle["selectedMessages"] = emptyList<Message>()
            }
        }
    }

    private fun clearSelection() {
        savedStateHandle["selectedMessages"] = emptyList<Message>()
    }

    private fun selectedMessage(message: Message) {
        if (selectedMessages.value.contains(message)) {
            savedStateHandle["selectedMessages"] = selectedMessages.value - message
            return
        }
        savedStateHandle["selectedMessages"] = selectedMessages.value + message
    }
}