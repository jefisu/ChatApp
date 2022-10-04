package com.jefisu.chatapp.features_chat.data.dto

import com.jefisu.chatapp.core.data.dto.UserDto
import kotlinx.serialization.Serializable

@Serializable
data class ChatDto(
    val users: List<UserDto>,
    val messages: List<MessageDto>,
    val createdAt: Long,
    val id: String
)