package com.jefisu.chatapp.features_auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class Login(
    val login: String,
    val password: String
)