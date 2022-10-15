package com.jefisu.chatapp.features_chat.domain.use_cases

import com.jefisu.chatapp.features_chat.domain.repository.ChatRepository

class GetChat(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: String) = try {
        repository.getChat(chatId)
    } catch (_: Exception) {
        null
    }
}