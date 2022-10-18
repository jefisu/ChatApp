package com.jefisu.chatapp.features_chat.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatDto(
    val userIds: List<String>,
    val messages: List<MessageDto>,
    val createdAt: Long,
    val id: String
)