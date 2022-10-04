package com.jefisu.chatapp.features_chat.domain.use_cases

import com.jefisu.chatapp.features_chat.domain.services.ChatSocketService

class SendMessage(
    private val service: ChatSocketService
) {
    suspend operator fun invoke(message: String) {
        service.sendMessage(message)
    }
}