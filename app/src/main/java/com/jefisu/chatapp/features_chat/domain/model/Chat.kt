package com.jefisu.chatapp.features_chat.domain.model

import android.os.Parcelable
import com.jefisu.chatapp.core.data.model.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(
    val userIds: List<String>,
    val messages: List<Message>,
    val createdAt: Long,
    val id: String
):  Parcelable