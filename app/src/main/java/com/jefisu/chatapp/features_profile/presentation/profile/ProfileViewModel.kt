package com.jefisu.chatapp.features_profile.presentation.profile

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.core.util.UiEvent
import com.jefisu.chatapp.destinations.LoginScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val prefs: SharedPreferences,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultChannel = Channel<UiEvent>()
    val resultEvent = _resultChannel.receiveAsFlow()

    val user = savedStateHandle.getStateFlow<User?>("user", null)

    fun loadChangesUser() {
        viewModelScope.launch {
            val parsedUser = prefs.getString("user", null)
            parsedUser?.let { json ->
                val user = Json.decodeFromString<User>(json)
                savedStateHandle["user"] = user
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            prefs.edit()
                .clear()
                .apply()
            _resultChannel.send(UiEvent.Navigate(LoginScreenDestination))
        }
    }
}