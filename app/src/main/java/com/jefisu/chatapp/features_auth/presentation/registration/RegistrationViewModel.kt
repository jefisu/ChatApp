package com.jefisu.chatapp.features_auth.presentation.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.chatapp.core.util.Resource
import com.jefisu.chatapp.core.util.UiEvent
import com.jefisu.chatapp.core.validate.ValidateUtil
import com.jefisu.chatapp.destinations.HomeScreenDestination
import com.jefisu.chatapp.features_auth.domain.use_cases.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    var state by mutableStateOf(RegistrationState())
        private set

    private val _resultChannel = Channel<UiEvent>()
    val resultEvent = _resultChannel.receiveAsFlow()

    fun onEvent(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.UsernameChanged -> {
                state = state.copy(username = event.value)
            }
            is RegistrationEvent.EmailChanged -> {
                state = state.copy(email = event.value)
            }
            is RegistrationEvent.PasswordChanged -> {
                state = state.copy(password = event.value)
            }
            is RegistrationEvent.SignUp -> signUp()
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val validateUsername = ValidateUtil.validateUsername(state.username)
            val validateEmail = ValidateUtil.validateEmail(state.email)
            val validatePassword = ValidateUtil.validatePassword(state.password)
            val hasError = listOf(validateUsername, validateEmail, validatePassword)
            if (hasError.any { !it.successful }) {
                _resultChannel.send(UiEvent.ShowBottomSheet)
                state = state.copy(
                    error = hasError.first { it.errorMessage != null }.errorMessage,
                    isLoading = false
                )
                return@launch
            }

            val result = authUseCases.signUp(
                username = state.username,
                email = state.email,
                password = state.password
            )
            when (result) {
                is Resource.Success -> {
                    _resultChannel.send(UiEvent.Navigate(HomeScreenDestination))
                }
                is Resource.Error -> {
                    state = state.copy(error = result.uiText)
                    _resultChannel.send(UiEvent.ShowBottomSheet)
                }
            }
            state = state.copy(isLoading = false)
        }
    }
}