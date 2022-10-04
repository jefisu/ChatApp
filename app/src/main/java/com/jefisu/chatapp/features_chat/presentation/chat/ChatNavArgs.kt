package com.jefisu.chatapp.features_chat.presentation.chat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatNavArgs(
    val chatId: String?,
    val ownerUsername: String,
    val recipientUsername: String
) : Parcelable