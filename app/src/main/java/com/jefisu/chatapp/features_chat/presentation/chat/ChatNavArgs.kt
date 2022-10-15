package com.jefisu.chatapp.features_chat.presentation.chat

import android.os.Parcelable
import com.jefisu.chatapp.features_chat.domain.model.Message
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatNavArgs(
    val ownerUsername: String,
    val recipientUsername: String,
    val recipientAvatarUrl: String?,
    val messages: ArrayList<Message>
) : Parcelable