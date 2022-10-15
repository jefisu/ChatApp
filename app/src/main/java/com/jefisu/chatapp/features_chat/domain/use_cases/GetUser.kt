package com.jefisu.chatapp.features_chat.domain.use_cases

import com.jefisu.chatapp.core.data.repository.SharedRepository

class GetUser(
    private val repository: SharedRepository
) {
    suspend operator fun invoke(username: String) = try {
        repository.getUser(username)
    } catch (_: Exception) {
        null
    }
}