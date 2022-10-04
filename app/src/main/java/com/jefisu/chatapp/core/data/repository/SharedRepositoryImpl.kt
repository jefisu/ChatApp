package com.jefisu.chatapp.core.data.repository

import com.jefisu.chatapp.core.data.dto.UserDto
import com.jefisu.chatapp.core.data.mapper.toUser
import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.core.util.ChatConstants
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class SharedRepositoryImpl(
    private val client: HttpClient
) : SharedRepository {

    override suspend fun getUser(username: String): User {
        val response = client.get {
            url("${ChatConstants.BASE_URL}/user")
            parameter("username", username)
        }
        val user = response.body<UserDto>()
        return user.toUser()
    }
}