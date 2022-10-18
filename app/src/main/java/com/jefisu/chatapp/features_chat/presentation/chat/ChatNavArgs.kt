package com.jefisu.chatapp.features_chat.presentation.chat

import android.os.Parcelable
import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.features_chat.domain.model.Message
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatNavArgs(
    val chatId: String?,
    val ownerId: String,
    val recipientUser: User?,
    val messages: ArrayList<Message>
) : Parcelable