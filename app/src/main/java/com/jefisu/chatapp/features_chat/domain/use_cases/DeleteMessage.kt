package com.jefisu.chatapp.features_chat.domain.use_cases

import com.jefisu.chatapp.core.util.Resource
import com.jefisu.chatapp.core.util.requestCatch
import com.jefisu.chatapp.features_chat.domain.model.Message
import com.jefisu.chatapp.features_chat.domain.repository.ChatRepository

class DeleteMessage(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: String, messages: List<Message>): Resource<String> {
        return requestCatch {
            repository.deleteMessage(chatId, messages)
        }
    }
}