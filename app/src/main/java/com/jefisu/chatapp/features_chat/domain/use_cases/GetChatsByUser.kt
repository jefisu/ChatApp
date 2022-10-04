package com.jefisu.chatapp.features_chat.domain.use_cases

import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.features_chat.domain.model.Chat
import com.jefisu.chatapp.features_chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetChatsByUser(
    private val repository: ChatRepository
) {
    operator fun invoke(user: User): Flow<List<Chat>> {
        return flow {
            try {
                emit(repository.chatsByUser(user))
            } catch (_: Exception) { }
        }
    }
}