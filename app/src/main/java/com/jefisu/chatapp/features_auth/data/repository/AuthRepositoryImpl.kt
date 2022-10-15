package com.jefisu.chatapp.features_auth.data.repository

import android.content.SharedPreferences
import com.jefisu.chatapp.core.data.dto.UserDto
import com.jefisu.chatapp.core.data.mapper.toUser
import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.core.util.ChatConstants
import com.jefisu.chatapp.features_auth.data.dto.Login
import com.jefisu.chatapp.features_auth.data.dto.Registration
import com.jefisu.chatapp.features_auth.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders

class AuthRepositoryImpl(
    private val client: HttpClient,
    private val prefs: SharedPreferences
) : AuthRepository {

    override suspend fun signUp(
        username: String,
        email: String,
        password: String
    ): User {
        client.post {
            url("${ChatConstants.BASE_URL}/signup")
            setBody(Registration(username, email, password))
        }
        return signIn(email, password)
    }

    override suspend fun signIn(email: String, password: String): User {
        val response = client.post {
            url("${ChatConstants.BASE_URL}/signin")
            setBody(Login(email, password))
        }
        val token = response.bodyAsText()
        prefs.edit()
            .putString("jwt-token", token)
            .apply()
        return auth(token)
    }

    override suspend fun auth(token: String): User {
        val response = client.get {
            url("${ChatConstants.BASE_URL}/auth")
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        val user = response.body<UserDto>()
        return user.toUser()
    }
}