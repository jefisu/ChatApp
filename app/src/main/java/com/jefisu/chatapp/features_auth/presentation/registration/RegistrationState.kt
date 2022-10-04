package com.jefisu.chatapp.features_auth.presentation.registration

import com.jefisu.chatapp.core.util.UiText

data class RegistrationState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val error: UiText? = UiText.unknownError(),
    val isLoading: Boolean = false
)