package com.jefisu.chatapp.features_auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class Registration(
    val username: String,
    val email: String,
    val password: String
)