package com.jefisu.chatapp.features_profile.presentation.edit_password

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.jefisu.chatapp.features_chat.core.components.CustomIconButton
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@RootNavGraph
@Destination(navArgsDelegate = EdiPasswordNavArg::class, style = CustomTransitions::class)
@Composable
fun EditPasswordScreen(
    sheetState: ModalBottomSheetState,
    scope: CoroutineScope,
    navigator: DestinationsNavigator,
    viewModel: EditPasswordViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(
        key1 = viewModel.resultEvent
    ) {
        viewModel.resultEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> navigator.navigateUp()
                is UiEvent.ShowBottomSheet -> {
                    sheetState.show()
                }
            }
        }
    }

    BottomSheet(
        state = sheetState,
        scope = scope,
        error = state.error?.asString().orEmpty()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomIconButton(
                    icon = Icons.Default.ArrowBack,
                    onClick = navigator::navigateUp
                )
                Text(
                    text = stringResource(R.string.change_password),
                    fontSize = 22.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = painterResource(R.drawable.ic_password),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(32.dp))
            CustomTextField(
                text = state.oldPassword,
                fieldName = stringResource(R.string.current_password),
                hint = stringResource(R.string.enter_current_password),
                isPassword = true,
                onTextChange = { viewModel.onEvent(EditPasswordEvent.EnteredOldPassword(it)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(24.dp))
            CustomTextField(
                text = state.newPassword,
                fieldName = stringResource(R.string.new_password),
                hint = stringResource(R.string.enter_the_new_password),
                isPassword = true,
                onTextChange = { viewModel.onEvent(EditPasswordEvent.EnteredNewPassword(it)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(24.dp))
            CustomTextField(
                text = state.repeatNewPassword,
                fieldName = stringResource(R.string.confirm_password),
                hint = stringResource(R.string.repeat_new_password_again),
                isPassword = true,
                onTextChange = { viewModel.onEvent(EditPasswordEvent.EnteredRepeatPassword(it)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 6.dp)
            ) {
                CustomButton(
                    text = stringResource(R.string.change),
                    onClick = { viewModel.onEvent(EditPasswordEvent.SaveChanges) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}