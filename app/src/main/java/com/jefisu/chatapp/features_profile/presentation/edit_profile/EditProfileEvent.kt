package com.jefisu.chatapp.features_profile.presentation.edit_profile

import java.io.InputStream

sealed class EditProfileEvent {
    data class EnteredName(val value: String) : EditProfileEvent()
    data class EnteredUsername(val value: String) : EditProfileEvent()
    data class EnteredEmail(val value: String) : EditProfileEvent()
    data class ChangeAvatar(val stream: InputStream) : EditProfileEvent()
    object SaveChanges : EditProfileEvent()
}