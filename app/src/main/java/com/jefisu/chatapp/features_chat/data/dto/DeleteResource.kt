package com.jefisu.chatapp.features_chat.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeleteResource(
    val id: String,
    val items: List<String>
)