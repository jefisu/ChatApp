package com.jefisu.chatapp.features_auth.presentation.splash

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.chatapp.core.util.Resource
import com.jefisu.chatapp.core.util.UiEvent
import com.jefisu.chatapp.core.util.requestCatch
import com.jefisu.chatapp.destinations.HomeScreenDestination
import com.jefisu.chatapp.destinations.LoginScreenDestination
import com.jefisu.chatapp.features_auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val prefs: SharedPreferences
) : ViewModel() {

    private val _resultEvent = Channel<UiEvent>()
    val resultEvent = _resultEvent.receiveAsFlow()

    init {
        verifyUserLogged()
    }

    private fun verifyUserLogged() {
        viewModelScope.launch {
            val token = prefs.getString("jwt-token", null)
            if (token == null) {
                _resultEvent.send(UiEvent.Navigate(LoginScreenDestination))
                return@launch
            }

            requestCatch {
                repository.auth(token)
            }.also { result ->
                when (result) {
                    is Resource.Success -> {
                        _resultEvent.send(UiEvent.Navigate(HomeScreenDestination(result.data!!)))
                    }
                    is Resource.Error -> {
                        _resultEvent.send(UiEvent.Navigate(LoginScreenDestination))
                    }
                }
            }
        }
    }
}