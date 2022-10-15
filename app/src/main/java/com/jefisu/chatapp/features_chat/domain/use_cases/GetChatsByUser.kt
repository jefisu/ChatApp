package com.jefisu.chatapp.features_chat.domain.use_cases

import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.features_chat.domain.repository.ChatRepository

class GetChatsByUser(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(user: User) = try {
        repository.chatsByUser(user)
    } catch (_: Exception) {
        emptyList()
    }
}