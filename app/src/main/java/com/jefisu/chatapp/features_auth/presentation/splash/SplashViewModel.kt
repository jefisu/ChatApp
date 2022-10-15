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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
                        val parsedUser = Json.encodeToString(result.data!!)
                        prefs.edit()
                            .putString("user", parsedUser)
                            .apply()
                        _resultEvent.send(UiEvent.Navigate(HomeScreenDestination))
                    }
                    is Resource.Error -> {
                        _resultEvent.send(UiEvent.Navigate(LoginScreenDestination))
                    }
                }
            }
        }
    }
}