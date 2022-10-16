package com.jefisu.chatapp.features_chat.presentation.home

import android.os.Parcelable
import com.jefisu.chatapp.features_chat.domain.model.Message
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeNavArg(
    val chatId: String?,
    val messages: ArrayList<Message>
): Parcelable