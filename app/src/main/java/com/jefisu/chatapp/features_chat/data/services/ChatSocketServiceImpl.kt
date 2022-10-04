package com.jefisu.chatapp.features_chat.data.services

import com.jefisu.chatapp.core.util.ChatConstants
import com.jefisu.chatapp.features_chat.data.dto.MessageDto
import com.jefisu.chatapp.features_chat.data.mapper.toMessage
import com.jefisu.chatapp.features_chat.domain.model.Message
import com.jefisu.chatapp.features_chat.domain.services.ChatSocketService
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ChatSocketServiceImpl(
    private val client: HttpClient
) : ChatSocketService {

    private var socket: WebSocketSession? = null

    override suspend fun initSession(
        senderUsername: String,
        recipientUsername: String
    ): Boolean {
        return try {
            socket = client.webSocketSession {
                url("${ChatConstants.WS_BASE_URL}/chat-socket")
                parameter("sender", senderUsername)
                parameter("recipient", recipientUsername)
            }
            socket?.isActive == true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun sendMessage(message: String) {
        try {
            socket?.send(Frame.Text(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun observeMessages(): Flow<Message> {
        return try {
            socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: ""
                    val messageDto = Json.decodeFromString<MessageDto>(json)
                    messageDto.toMessage()
                } ?: flowOf()
        } catch (e: Exception) {
            e.printStackTrace()
            flowOf()
        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }
}