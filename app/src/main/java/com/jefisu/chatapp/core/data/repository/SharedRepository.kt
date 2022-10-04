package com.jefisu.chatapp.core.data.repository

import com.jefisu.chatapp.core.data.model.User

interface SharedRepository {

    suspend fun getUser(username: String): User
}