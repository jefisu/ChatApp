package com.jefisu.chatapp.features_chat.domain.use_cases

import com.jefisu.chatapp.features_chat.domain.services.ChatSocketService

class ConnectToChat(
    private val service: ChatSocketService
) {
    suspend operator fun invoke(
        senderId: String,
        recipientId: String
    ): Boolean {
        return service.initSession(senderId, recipientId)
    }
}