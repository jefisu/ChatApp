package com.jefisu.chatapp.features_chat.presentation.chat

import com.jefisu.chatapp.features_chat.domain.model.Message

data class ChatState(
    val messages: List<Message> = emptyList(),
    val ownerUsername: String = "",
    val recipientUsername: String = "",
    val recipientAvatarUrl: String? = null,
    val messageText: String = "",
    val isBlankText: Boolean = messageText.isBlank()
)