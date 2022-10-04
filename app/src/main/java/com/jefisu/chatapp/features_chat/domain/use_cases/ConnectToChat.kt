package com.jefisu.chatapp.features_chat.domain.use_cases

import com.jefisu.chatapp.features_chat.domain.services.ChatSocketService

class ConnectToChat(
    private val service: ChatSocketService
) {
    suspend operator fun invoke(
        ownerUsername: String,
        recipientUsername: String
    ): Boolean {
        return service.initSession(ownerUsername, recipientUsername)
    }
}