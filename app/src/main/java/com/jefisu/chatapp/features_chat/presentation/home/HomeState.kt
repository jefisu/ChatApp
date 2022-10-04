package com.jefisu.chatapp.features_chat.presentation.home

import com.jefisu.chatapp.core.data.model.User

data class HomeState(
    val ownerUser: User,
    val chats: List<ChatPreview>,
    val users: List<User>
)