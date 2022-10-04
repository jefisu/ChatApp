package com.jefisu.chatapp.features_profile.presentation.profile

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.core.util.Resource
import com.jefisu.chatapp.core.util.UiEvent
import com.jefisu.chatapp.destinations.LoginScreenDestination
import com.jefisu.chatapp.features_profile.domain.use_cases.ProfileUseCase
import com.jefisu.chatapp.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val prefs: SharedPreferences,
    private val profileUseCase: ProfileUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultChannel = Channel<UiEvent>()
    val resultEvent = _resultChannel.receiveAsFlow()

    val user = savedStateHandle.getStateFlow("user", savedStateHandle.navArgs<User>())

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.Logout -> logout()
            is ProfileEvent.ChangeAvatar -> changeAvatar(event.stream)
        }
    }

    fun loadChangesUser() {
        viewModelScope.launch {
            val username = prefs.getString("username", null) ?: return@launch
            val result = profileUseCase.getChangesUser(username)
            when (result) {
                is Resource.Success -> {
                    savedStateHandle["user"] = result.data
                }
                is Resource.Error -> {
                    _resultChannel.send(UiEvent.Navigate())
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            prefs.edit()
                .clear()
                .apply()
            _resultChannel.send(UiEvent.Navigate(LoginScreenDestination))
        }
    }

    private fun changeAvatar(stream: InputStream) {
        viewModelScope.launch {
            val result = profileUseCase.changeAvatar(user.value, stream.readBytes())
            when (result) {
                is Resource.Success -> {
                    savedStateHandle["user"] = result.data
                }
                is Resource.Error -> Unit
            }
        }
    }
}