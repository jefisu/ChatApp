package com.jefisu.chatapp.features_chat.presentation.chat

import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.features_chat.domain.model.Message

data class ChatState(
    val chatId: String? = null,
    val messages: List<Message> = emptyList(),
    val ownerId: String = "",
    val recipientUser: User? = null,
    val messageText: String = "",
    val isBlankText: Boolean = messageText.isBlank(),
    val selectedMessages: List<Message> = emptyList(),
    val searchMessageQuery: String = ""
)