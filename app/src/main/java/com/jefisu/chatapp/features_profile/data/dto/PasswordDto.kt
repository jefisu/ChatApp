package com.jefisu.chatapp.features_profile.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PasswordDto(
    val username: String,
    val oldPassword: String,
    val newPassword: String
)