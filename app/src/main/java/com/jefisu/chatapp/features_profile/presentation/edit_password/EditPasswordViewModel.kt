package com.jefisu.chatapp.features_profile.presentation.edit_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.chatapp.core.util.Resource
import com.jefisu.chatapp.core.util.UiEvent
import com.jefisu.chatapp.core.validate.ValidateUtil
import com.jefisu.chatapp.features_profile.domain.model.Password
import com.jefisu.chatapp.features_profile.domain.use_cases.ProfileUseCase
import com.jefisu.chatapp.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPasswordViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArg = savedStateHandle.navArgs<EdiPasswordNavArg>()

    var state by mutableStateOf(EditPasswordState())
        private set

    private val _resultChannel = Channel<UiEvent>()
    val resultEvent = _resultChannel.receiveAsFlow()

    fun onEvent(event: EditPasswordEvent) {
        when (event) {
            is EditPasswordEvent.EnteredOldPassword -> {
                state = state.copy(oldPassword = event.value)
            }
            is EditPasswordEvent.EnteredNewPassword -> {
                state = state.copy(newPassword = event.value)
            }
            is EditPasswordEvent.EnteredRepeatPassword -> {
                state = state.copy(repeatNewPassword = event.value)
            }
            is EditPasswordEvent.SaveChanges -> changePassword()
        }
    }

    private fun changePassword() {
        viewModelScope.launch {
            val request = Password(
                username = navArg.username,
                oldPassword = state.oldPassword,
                newPassword = state.newPassword
            )

            val validatePassword = ValidateUtil.validatePassword(state.newPassword, state.repeatNewPassword)
            if (!validatePassword.successful) {
                state = state.copy(
                    error = validatePassword.errorMessage
                )
                _resultChannel.send(UiEvent.ShowBottomSheet)
                return@launch
            }

            val result = profileUseCase.changePassword(request)
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
    }
}