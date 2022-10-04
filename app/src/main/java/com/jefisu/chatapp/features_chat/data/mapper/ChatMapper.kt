package com.jefisu.chatapp.features_chat.data.mapper

import com.jefisu.chatapp.core.data.mapper.toUser
import com.jefisu.chatapp.features_chat.data.dto.ChatDto
import com.jefisu.chatapp.features_chat.domain.model.Chat

fun ChatDto.toChat(): Chat {
    return Chat(
        users = users.map { it.toUser() },
        messages = messages.map { it.toMessage() },
        createdAt = createdAt,
        id = id
    )
}