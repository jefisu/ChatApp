package com.jefisu.chatapp.core.util

import com.jefisu.chatapp.features_chat.domain.model.Message

fun Iterable<Message>.indexesOf(query: String) =
    mapIndexedNotNull { index, message -> index.takeIf { message.text.contains(query) } }

fun Iterable<Message>.quantityMessagesLocated(query: String) = count {
    if (!it.text.contains(query) && query.isBlank()) {
        return@count true
    }
    it.text.contains(query)
}