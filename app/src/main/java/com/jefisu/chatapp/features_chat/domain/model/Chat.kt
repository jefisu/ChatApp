package com.jefisu.chatapp.features_chat.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(
    val userIds: List<String>,
    val messages: List<Message>,
    val createdAt: Long,
    val id: String
) : Parcelable