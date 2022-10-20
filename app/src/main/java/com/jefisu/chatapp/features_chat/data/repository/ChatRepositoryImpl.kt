package com.jefisu.chatapp.features_chat.data.repository

import com.jefisu.chatapp.core.data.dto.UserDto
import com.jefisu.chatapp.core.data.mapper.toUser
import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.core.util.ChatConstants
import com.jefisu.chatapp.features_chat.data.dto.ChatDto
import com.jefisu.chatapp.features_chat.data.mapper.toChat
import com.jefisu.chatapp.features_chat.data.mapper.toMessageId
import com.jefisu.chatapp.features_chat.domain.model.Chat
import com.jefisu.chatapp.features_chat.domain.model.Message
import com.jefisu.chatapp.features_chat.domain.repository.ChatRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ChatRepositoryImpl(
    private val client: HttpClient
) : ChatRepository {

    override suspend fun chatsByUser(userId: String): List<Chat> {
        val response = client.post {
            url("${ChatConstants.BASE_URL}/chats")
            parameter("userId", userId)
        }
        val chats = response.body<List<ChatDto>>()
        return chats.map { it.toChat() }
    }

    override suspend fun getChat(chatId: String): Chat {
        val response = client.get {
            url("${ChatConstants.BASE_URL}/chat")
            parameter("chatId", chatId)
        }
        val chat = response.body<ChatDto>()
        return chat.toChat()
    }

    override suspend fun getAllUsers(ownerUsername: String): List<User> {
        val response = client.get {
            url("${ChatConstants.BASE_URL}/users")
            parameter("ownerUsername", ownerUsername)
        }
        val users = response.body<List<UserDto>>()
        return users.map { it.toUser() }
    }

    override suspend fun deleteChat(chatId: String): String {
        val response = client.delete {
            url("${ChatConstants.BASE_URL}/chat/bin")
            parameter("chatId", chatId)
        }
        return response.body()
    }

    override suspend fun deleteMessage(chatId: String, messages: List<Message>): String {
        val response = client.delete {
            url("${ChatConstants.BASE_URL}/chat/message")
            parameter("chatId", chatId)
            val messageIds = Json.encodeToString(messages.map { it.toMessageId() })
            setBody(messageIds)
        }
        return response.body()
    }

    override suspend fun clearChat(chatId: String): String {
        val response = client.delete {
            url("${ChatConstants.BASE_URL}/chat/clear-messages")
            parameter("chatId", chatId)
        }
        return response.body()
    }
}