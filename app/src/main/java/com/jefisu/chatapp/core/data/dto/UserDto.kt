package com.jefisu.chatapp.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val name: String?,
    val username: String,
    val email: String,
    val avatarUrl: String?,
    val id: String
)