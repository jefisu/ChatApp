package com.jefisu.chatapp.features_auth.presentation.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jefisu.chatapp.R
import com.jefisu.chatapp.core.presentation.components.BottomSheet
import com.jefisu.chatapp.core.presentation.components.CustomButton
import com.jefisu.chatapp.core.presentation.components.CustomTextField
import com.jefisu.chatapp.core.util.CustomTransitions
import com.jefisu.chatapp.core.util.UiEvent
import com.jefisu.chatapp.destinations.LoginScreenDestination
import com.jefisu.chatapp.ui.theme.GhostWhite
import com.jefisu.chatapp.ui.theme.QuickSilver
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@RootNavGraph
@Destination(style = CustomTransitions::class)
@Composable
fun RegistrationScreen(
    scope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    navigator: DestinationsNavigator,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(key1 = viewModel.resultEvent) {
        viewModel.resultEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    navigator.apply {
                        clearBackStack(LoginScreenDestination)
                        popBackStack()
                        event.destination?.let { navigate(it) }
                    }
                }
                is UiEvent.ShowBottomSheet -> {
                    sheetState.show()
                }
            }
        }
    }

    BottomSheet(
        state = sheetState,
        scope = scope,
        error = state.error!!.asString()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = stringResource(R.string.create_account, ","),
                fontSize = 36.sp,
                color = GhostWhite,
                modifier = Modifier.padding(
                    start = 8.dp
                )
            )
            Text(
                text = stringResource(R.string.sign_up_to_get_started),
                fontSize = 24.sp,
                color = QuickSilver,
                modifier = Modifier.padding(
                    start = 8.dp
                )
            )
            Spacer(modifier = Modifier.height(40.dp))
            CustomTextField(
                text = state.username,
                fieldName = stringResource(R.string.username),
                hint = stringResource(R.string.hint_username),
                onTextChange = { viewModel.onEvent(RegistrationEvent.UsernameChanged(it)) },
            )
            Spacer(modifier = Modifier.height(24.dp))
            CustomTextField(
                text = state.email,
                fieldName = stringResource(R.string.email),
                hint = stringResource(R.string.hint_email),
                onTextChange = { viewModel.onEvent(RegistrationEvent.EmailChanged(it)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            CustomTextField(
                text = state.password,
                fieldName = stringResource(R.string.password),
                hint = stringResource(R.string.hint_password),
                onTextChange = { viewModel.onEvent(RegistrationEvent.PasswordChanged(it)) },
                isPassword = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(36.dp))
            CustomButton(
                text = stringResource(R.string.create_account, ""),
                isLoading = state.isLoading,
                onClick = { viewModel.onEvent(RegistrationEvent.SignUp) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}