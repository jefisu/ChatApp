package com.jefisu.chatapp.features_profile.presentation.edit_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.core.util.Resource
import com.jefisu.chatapp.core.util.UiEvent
import com.jefisu.chatapp.core.validate.ValidateUtil
import com.jefisu.chatapp.features_profile.domain.use_cases.ProfileUseCase
import com.jefisu.chatapp.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val navArgUser = savedStateHandle.navArgs<User>()

    var state by mutableStateOf(EditProfileState())
        private set

    private val _resultChannel = Channel<UiEvent>()
    val resultEvent = _resultChannel.receiveAsFlow()

    init {
        state = state.copy(
            name = navArgUser.name.orEmpty(),
            username = navArgUser.username,
            email = navArgUser.email
        )
    }

    fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.EnteredName -> {
                state = state.copy(name = event.value)
            }
            is EditProfileEvent.EnteredUsername -> {
                state = state.copy(username = event.value)
            }
            is EditProfileEvent.EnteredEmail -> {
                state = state.copy(email = event.value)
            }
            is EditProfileEvent.ChangeAvatar -> {
                state = state.copy(avatarStream = event.stream)
            }
            is EditProfileEvent.SaveChanges -> saveChanges()
        }
    }

    private fun saveChanges() {
        viewModelScope.launch {
            val user = navArgUser.copy(
                name = if (state.name.isBlank() || state.name.isEmpty()) null else state.name,
                username = state.username,
                email = state.email
            )
            val validateEmail = ValidateUtil.validateEmail(state.email)
            val validateUsername = ValidateUtil.validateUsername(state.username)
            val hasError = listOf(validateUsername, validateEmail)
            if (hasError.any { !it.successful }) {
                state = state.copy(
                    error = hasError.first { !it.successful }.errorMessage
                )
                _resultChannel.send(UiEvent.ShowBottomSheet)
                return@launch
            }

            launch {
                val result = profileUseCase.changeUserInfo(user)
                when (result) {
                    is Resource.Success -> {
                        _resultChannel.send(UiEvent.Navigate())
                    }
                    is Resource.Error -> {
                        _resultChannel.send(UiEvent.ShowBottomSheet)
                        state = state.copy(
                            error = result.uiText
                        )
                    }
                }
            }
            launch {
                state.avatarStream?.let {
                    val result = profileUseCase.changeAvatar(user, it.readBytes())
                    when (result) {
                        is Resource.Success -> Unit
                        is Resource.Error -> {
                            _resultChannel.send(UiEvent.ShowBottomSheet)
                            state = state.copy(
                                error = result.uiText
                            )
                        }
                    }
                }
            }
        }
    }
}