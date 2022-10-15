package com.jefisu.chatapp.features_profile.data.repository

import com.jefisu.chatapp.core.data.mapper.toUserDto
import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.core.util.ChatConstants
import com.jefisu.chatapp.features_profile.data.mapper.toPasswordDto
import com.jefisu.chatapp.features_profile.domain.model.Password
import com.jefisu.chatapp.features_profile.domain.repository.ProfileRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.append
import io.ktor.client.request.forms.formData
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.util.generateNonce
import io.ktor.utils.io.core.writeFully

class ProfileRepositoryImpl(
    private val client: HttpClient
) : ProfileRepository {

    override suspend fun changeAvatar(user: User, data: ByteArray): String {
        val response = client.put {
            url("${ChatConstants.BASE_URL}/user/change-avatar")
            parameter("userId", user.id)
            setBody(MultiPartFormDataContent(formData {
                append(key = "avatarImage",
                    filename = "${generateNonce()}.png",
                    bodyBuilder = { writeFully(data) })
            }))
        }
        return response.bodyAsText()
    }

    override suspend fun changeUserInfo(user: User): String {
        val response = client.put {
            url("${ChatConstants.BASE_URL}/user/change-info")
            setBody(user.toUserDto())
        }
        return response.bodyAsText()
    }

    override suspend fun changePassword(request: Password): String {
        val response = client.put {
            url("${ChatConstants.BASE_URL}/user/change-password")
            setBody(request.toPasswordDto())
        }
        return response.bodyAsText()
    }
}