package com.jefisu.chatapp.features_profile.presentation.edit_profile

import com.jefisu.chatapp.core.util.UiText
import java.io.InputStream

data class EditProfileState(
    val avatarStream: InputStream? = null,
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val error: UiText? = null
)