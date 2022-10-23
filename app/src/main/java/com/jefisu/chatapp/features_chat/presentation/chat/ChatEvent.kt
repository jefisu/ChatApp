package com.jefisu.chatapp.features_chat.presentation.chat

import com.jefisu.chatapp.features_chat.domain.model.Message

sealed class ChatEvent {
    data class MessageTextChange(val value: String) : ChatEvent()
    object SendMessage : ChatEvent()
    data class SelectMessage(val message: Message) : ChatEvent()
    object ClearSelectionMessages : ChatEvent()
    object DeleteMessages : ChatEvent()
    object ClearHistory : ChatEvent()
    data class SearchMessage(val query: String) : ChatEvent()
}