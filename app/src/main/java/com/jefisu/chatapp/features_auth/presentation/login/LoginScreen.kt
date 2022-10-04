package com.jefisu.chatapp.features_auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.jefisu.chatapp.destinations.RegistrationScreenDestination
import com.jefisu.chatapp.ui.theme.GhostWhite
import com.jefisu.chatapp.ui.theme.MaximumPurple
import com.jefisu.chatapp.ui.theme.QuickSilver
import com.jefisu.chatapp.ui.theme.TaupeGray
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@RootNavGraph
@Destination(style = CustomTransitions::class)
@Composable
fun LoginScreen(
    scope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    navigator: DestinationsNavigator,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(
        key1 = viewModel.authEvent
    ) {
        viewModel.authEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    navigator.apply {
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
                text = stringResource(R.string.welcome),
                fontSize = 36.sp,
                color = GhostWhite,
                modifier = Modifier.padding(
                    start = 8.dp
                )
            )
            Text(
                text = stringResource(R.string.sign_in_to_continue),
                fontSize = 24.sp,
                color = QuickSilver,
                modifier = Modifier.padding(
                    start = 8.dp
                )
            )
            Spacer(modifier = Modifier.height(90.dp))
            CustomTextField(
                text = state.email,
                fieldName = stringResource(R.string.email),
                hint = stringResource(R.string.hint_email),
                onTextChange = { viewModel.onEvent(LoginEvent.EmailChanged(it)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            CustomTextField(
                text = state.password,
                fieldName = stringResource(R.string.password),
                hint = stringResource(R.string.enter_your_password),
                onTextChange = { viewModel.onEvent(LoginEvent.PasswordChanged(it)) },
                isPassword = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(36.dp))
            CustomButton(
                text = stringResource(R.string.login),
                isLoading = state.isLoading,
                onClick = {
                    viewModel.onEvent(LoginEvent.SignIn)
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(100.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.dont_have_an_account),
                    color = TaupeGray,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.sign_up),
                    color = MaximumPurple,
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        navigator.navigate(RegistrationScreenDestination)
                    }
                )
            }
        }
    }
}