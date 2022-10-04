package com.jefisu.chatapp.features_profile.presentation.edit_password

sealed class EditPasswordEvent {
    data class EnteredOldPassword(val value: String) : EditPasswordEvent()
    data class EnteredNewPassword(val value: String) : EditPasswordEvent()
    data class EnteredRepeatPassword(val value: String) : EditPasswordEvent()
    object SaveChanges : EditPasswordEvent()
}