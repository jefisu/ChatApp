package com.jefisu.chatapp.features_chat.domain.use_cases

import com.jefisu.chatapp.features_chat.domain.repository.ChatRepository

class GetAllUsers(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(username: String) = try {
        repository.getAllUsers(username)
    } catch (_: Exception) {
        emptyList()
    }
}