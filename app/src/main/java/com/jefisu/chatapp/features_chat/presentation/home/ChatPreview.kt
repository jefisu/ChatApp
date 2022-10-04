package com.jefisu.chatapp.features_chat.presentation.home

import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.features_chat.domain.model.Message

data class ChatPreview(
    val id: String,
    val recipientUser: User,
    val lastMessage: Message?,
    val ownerSentLastMessage: Boolean,
    val timeLastMessage: String
)