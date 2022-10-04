package com.jefisu.chatapp.features_chat.domain.use_cases

import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.features_chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAllUsers(
    private val repository: ChatRepository
) {
    operator fun invoke(username: String): Flow<List<User>> {
        return flow {
            try {
                emit(repository.getAllUsers(username))
            } catch (_: Exception) { }
        }
    }
}