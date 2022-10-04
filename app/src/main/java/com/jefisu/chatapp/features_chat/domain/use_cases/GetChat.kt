package com.jefisu.chatapp.features_chat.domain.use_cases

import com.jefisu.chatapp.features_chat.domain.model.Chat
import com.jefisu.chatapp.features_chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetChat(
    private val repository: ChatRepository
) {
    operator fun invoke(chatId: String): Flow<Chat> {
        return flow {
            try {
                emit(repository.getChat(chatId))
            } catch (_: Exception) {}
        }
    }
}