package com.jefisu.chatapp.features_profile.domain.model

data class Password(
    val username: String,
    val oldPassword: String,
    val newPassword: String
)