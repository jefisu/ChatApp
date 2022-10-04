package com.jefisu.chatapp.features_chat.domain.use_cases

import com.jefisu.chatapp.features_chat.domain.services.ChatSocketService

class ExitChat(
    private val service: ChatSocketService
) {
    suspend operator fun invoke() {
        service.closeSession()
    }
}