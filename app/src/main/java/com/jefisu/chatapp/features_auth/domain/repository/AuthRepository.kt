package com.jefisu.chatapp.features_auth.domain.repository

import com.jefisu.chatapp.core.data.model.User

interface AuthRepository {

    suspend fun signUp(
        username: String,
        email: String,
        password: String
    ): User

    suspend fun signIn(email: String, password: String): User

    suspend fun auth(token: String): User
}