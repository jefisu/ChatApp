package com.jefisu.chatapp.features_profile.domain.repository

import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.features_profile.domain.model.Password

interface ProfileRepository {

    suspend fun changeAvatar(user: User, data: ByteArray): String

    suspend fun changeUserInfo(user: User): String

    suspend fun changePassword(request: Password): String
}