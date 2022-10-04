package com.jefisu.chatapp.features_profile.presentation.edit_password

import com.jefisu.chatapp.core.util.UiText

data class EditPasswordState(
    val oldPassword: String = "",
    val newPassword: String = "",
    val repeatNewPassword: String = "",
    val error: UiText? = null
)