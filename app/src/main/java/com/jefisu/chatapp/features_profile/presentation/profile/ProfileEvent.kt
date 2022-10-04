package com.jefisu.chatapp.features_profile.presentation.profile

import java.io.InputStream

sealed class ProfileEvent {
    object Logout : ProfileEvent()
    data class ChangeAvatar(val stream: InputStream) : ProfileEvent()
}