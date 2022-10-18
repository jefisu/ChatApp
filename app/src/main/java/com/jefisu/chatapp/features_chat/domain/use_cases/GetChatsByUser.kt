package com.jefisu.chatapp.features_chat.domain.use_cases

import com.jefisu.chatapp.features_chat.domain.repository.ChatRepository

class GetChatsByUser(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(userId: String) = try {
        repository.chatsByUser(userId)
    } catch (_: Exception) {
        emptyList()
    }
}