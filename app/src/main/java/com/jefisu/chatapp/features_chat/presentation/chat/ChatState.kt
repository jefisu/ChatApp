package com.jefisu.chatapp.features_chat.presentation.chat

import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.core.util.UiText
import com.jefisu.chatapp.features_chat.domain.model.Message

data class ChatState(
    val messageText: String = "",
    val messages: List<Message> = emptyList(),
    val error: UiText? = null,
    val isLoading: Boolean = false,
    val ownerUsername: String = "",
    val recipientUser: User? = null
)