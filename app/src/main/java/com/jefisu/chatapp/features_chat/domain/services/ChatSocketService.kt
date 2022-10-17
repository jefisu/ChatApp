package com.jefisu.chatapp.features_chat.domain.services

import com.jefisu.chatapp.features_chat.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatSocketService {

    suspend fun initSession(
        senderId: String,
        recipientId: String
    ): Boolean

    suspend fun sendMessage(message: String)

    fun observeMessages(): Flow<Message>

    suspend fun closeSession()

    companion object {
        const val BASE_URL = "ws://192.168.0.2:8080"
    }
}