package com.jefisu.chatapp.features_auth.presentation.login

sealed class LoginEvent {
    data class EmailChanged(val value: String) : LoginEvent()
    data class PasswordChanged(val value: String) : LoginEvent()
    object SignIn : LoginEvent()
}