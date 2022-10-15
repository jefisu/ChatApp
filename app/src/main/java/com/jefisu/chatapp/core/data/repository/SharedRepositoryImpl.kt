package com.jefisu.chatapp.core.data.repository

import android.content.SharedPreferences
import com.jefisu.chatapp.core.data.dto.UserDto
import com.jefisu.chatapp.core.data.mapper.toUser
import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.core.util.ChatConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SharedRepositoryImpl(
    private val client: HttpClient,
    private val prefs: SharedPreferences
) : SharedRepository {

    override suspend fun getUser(username: String): User {
        val response = client.get {
            url("${ChatConstants.BASE_URL}/user")
            parameter("username", username)
        }
        val user = response.body<UserDto>()
        val parsedUser = Json.encodeToString(user.toUser())
        prefs.edit()
            .putString("user", parsedUser)
            .apply()
        return user.toUser()
    }
}