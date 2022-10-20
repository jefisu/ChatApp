package com.jefisu.chatapp.features_chat.domain.repository

import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.features_chat.data.dto.DeleteResource
import com.jefisu.chatapp.features_chat.domain.model.Chat

interface ChatRepository {

    suspend fun chatsByUser(userId: String): List<Chat>

    suspend fun getChat(chatId: String): Chat

    suspend fun getAllUsers(ownerUsername: String): List<User>

    suspend fun deleteChat(deleteResource: DeleteResource): String
    suspend fun deleteMessage(deleteResource: DeleteResource): String
    suspend fun clearChat(chatId: String): String
}