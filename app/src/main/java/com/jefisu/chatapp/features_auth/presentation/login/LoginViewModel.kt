package com.jefisu.chatapp.features_auth.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.chatapp.core.util.Resource
import com.jefisu.chatapp.core.util.UiEvent
import com.jefisu.chatapp.destinations.HomeScreenDestination
import com.jefisu.chatapp.features_auth.domain.use_cases.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private val _resultChannel = Channel<UiEvent>()
    val authEvent = _resultChannel.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                state = state.copy(
                    email = event.value
                )
            }
            is LoginEvent.PasswordChanged -> {
                state = state.copy(
                    password = event.value
                )
            }
            is LoginEvent.SignIn -> signIn()
        }
    }

    private fun signIn() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = authUseCases.signIn(state.email, state.password)
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