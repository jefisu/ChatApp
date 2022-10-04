package com.jefisu.chatapp.features_auth.presentation.registration

sealed class RegistrationEvent {
    data class UsernameChanged(val value: String) : RegistrationEvent()
    data class EmailChanged(val value: String) : RegistrationEvent()
    data class PasswordChanged(val value: String) : RegistrationEvent()
    object SignUp : RegistrationEvent()
}