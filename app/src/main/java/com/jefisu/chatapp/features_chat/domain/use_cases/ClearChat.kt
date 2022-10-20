package com.jefisu.chatapp.features_chat.domain.use_cases

import com.jefisu.chatapp.core.util.Resource
import com.jefisu.chatapp.core.util.requestCatch
import com.jefisu.chatapp.features_chat.domain.repository.ChatRepository

class ClearChat(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: String): Resource<String> {
        return requestCatch {
            repository.clearChat(chatId)
        }
    }
}