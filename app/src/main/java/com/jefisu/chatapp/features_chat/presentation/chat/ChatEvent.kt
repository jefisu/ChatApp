package com.jefisu.chatapp.features_chat.presentation.chat

sealed class ChatEvent {
    data class MessageTextChange(val value: String) : ChatEvent()
    object SendMessage : ChatEvent()
    object ClearText : ChatEvent()
}