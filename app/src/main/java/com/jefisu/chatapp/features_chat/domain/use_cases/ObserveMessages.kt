package com.jefisu.chatapp.features_chat.domain.use_cases

import com.jefisu.chatapp.features_chat.domain.model.Message
import com.jefisu.chatapp.features_chat.domain.services.ChatSocketService
import kotlinx.coroutines.flow.Flow

class ObserveMessages(
    private val service: ChatSocketService
) {
    operator fun invoke(): Flow<Message> {
        return service.observeMessages()
    }
}