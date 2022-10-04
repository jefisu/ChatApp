package com.jefisu.chatapp.features_chat.data.repository

import com.jefisu.chatapp.core.data.dto.UserDto
import com.jefisu.chatapp.core.data.mapper.toUser
import com.jefisu.chatapp.core.data.mapper.toUserDto
import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.core.util.ChatConstants
import com.jefisu.chatapp.features_chat.data.dto.ChatDto
import com.jefisu.chatapp.features_chat.data.mapper.toChat
import com.jefisu.chatapp.features_chat.domain.model.Chat
import com.jefisu.chatapp.features_chat.domain.repository.ChatRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class ChatRepositoryImpl(
    private val client: HttpClient
) : ChatRepository {

    override suspend fun chatsByUser(user: User): List<Chat> {
        val response = client.post {
            url("${ChatConstants.BASE_URL}/chats")
            setBody(user.toUserDto())
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
}