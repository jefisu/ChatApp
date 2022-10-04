package com.jefisu.chatapp.features_auth.presentation.login

import com.jefisu.chatapp.core.util.UiText

data class LoginState(
    val email: String = "",
    val password: String = "",
    val error: UiText? = UiText.unknownError(),
    val isLoading: Boolean = false
)