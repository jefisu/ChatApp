package com.jefisu.chatapp.features_chat.domain.repository

import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.features_chat.domain.model.Chat
import com.jefisu.chatapp.features_chat.domain.model.Message

interface ChatRepository {

    suspend fun chatsByUser(userId: String): List<Chat>

    suspend fun getChat(chatId: String): Chat

    suspend fun getAllUsers(ownerUsername: String): List<User>

    suspend fun deleteChat(chatId: String): String
    suspend fun deleteMessage(chatId: String, messages: List<Message>): String
    suspend fun clearChat(chatId: String): String
}