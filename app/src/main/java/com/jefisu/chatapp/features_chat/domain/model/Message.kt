package com.jefisu.chatapp.features_chat.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    val text: String,
    val userId: String,
    val timestamp: Long,
    val id: String
) : Parcelable