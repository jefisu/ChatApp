package com.jefisu.chatapp.features_chat.data.services

import com.jefisu.chatapp.core.util.ChatConstants
import com.jefisu.chatapp.features_chat.data.dto.MessageDto
import com.jefisu.chatapp.features_chat.data.mapper.toMessage
import com.jefisu.chatapp.features_chat.domain.model.Message
import com.jefisu.chatapp.features_chat.domain.services.ChatSocketService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ChatSocketServiceImpl(
    private val client: HttpClient
) : ChatSocketService {

    private var socket: WebSocketSession? = null

    override suspend fun initSession(
        senderId: String,
        recipientId: String
    ): Boolean {
        return try {
            socket = client.webSocketSession {
                url("${ChatConstants.WS_BASE_URL}/chat-socket")
                parameter("senderId", senderId)
                parameter("recipientId", recipientId)
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
        } catch (_: Exception) {
            flowOf()
        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }
}