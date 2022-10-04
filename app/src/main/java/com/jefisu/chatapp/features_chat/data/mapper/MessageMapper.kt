package com.jefisu.chatapp.features_chat.data.mapper

import com.jefisu.chatapp.features_chat.data.dto.MessageDto
import com.jefisu.chatapp.features_chat.domain.model.Message

fun MessageDto.toMessage(): Message {
    return Message(
        text = text,
        username = username,
        id = id,
        timestamp = timestamp
    )
}