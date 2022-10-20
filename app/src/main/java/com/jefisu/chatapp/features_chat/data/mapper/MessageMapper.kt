package com.jefisu.chatapp.features_chat.data.mapper

import com.jefisu.chatapp.features_chat.data.dto.MessageDto
import com.jefisu.chatapp.features_chat.data.dto.MessageId
import com.jefisu.chatapp.features_chat.domain.model.Message

fun MessageDto.toMessage(): Message {
    return Message(
        text = text,
        userId = userId,
        id = id,
        timestamp = timestamp
    )
}

fun Message.toMessageId(): MessageId {
    return MessageId(
       id = id
    )
}