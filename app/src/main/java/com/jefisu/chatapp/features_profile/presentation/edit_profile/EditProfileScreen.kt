package com.jefisu.chatapp.features_profile.presentation.edit_profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jefisu.chatapp.R
import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.core.presentation.components.BottomSheet
import com.jefisu.chatapp.core.presentation.components.CustomButton
import com.jefisu.chatapp.core.presentation.components.CustomTextField
import com.jefisu.chatapp.core.util.CustomTransitions
import com.jefisu.chatapp.core.util.UiEvent
import com.jefisu.chatapp.features_chat.core.components.CustomIconButton
import com.jefisu.chatapp.features_chat.core.components.ProfileImage
import com.jefisu.chatapp.ui.theme.VampireBlack
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@RootNavGraph
@Destination(navArgsDelegate = User::class, style = CustomTransitions::class)
@Composable
fun EditProfileScreen(
    sheetState: ModalBottomSheetState,
    scope: CoroutineScope,
    navigator: DestinationsNavigator,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val user = viewModel.navArgUser
    val context = LocalContext.current
    var uriFile by remember {
        mutableStateOf<Uri?>(null)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uriFile = uri
            val pickedFile = uri?.let(context.contentResolver::openInputStream)
            pickedFile?.let { viewModel.onEvent(EditProfileEvent.ChangeAvatar(it)) }
        }
    )

    LaunchedEffect(key1 = viewModel.resultEvent) {
        viewModel.resultEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> navigator.navigateUp()
                is UiEvent.ShowBottomSheet -> sheetState.show()
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
                    text = stringResource(R.string.profile),
                    fontSize = 22.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            ProfileImage(
                avatarUrl = uriFile?.toString() ?: user.avatarUrl,
                size = 130.dp,
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                iconCorner = Icons.Default.CameraAlt,
                iconCornerBackground = VampireBlack,
                iconCornerSpace = 0.dp,
                iconCornerSize = 24.dp,
                iconCornerBackgroundSize = 40.dp
            )
            Spacer(modifier = Modifier.height(32.dp))
            CustomTextField(
                text = state.name,
                fieldName = stringResource(R.string.name),
                hint = stringResource(R.string.hint_name),
                onTextChange = { viewModel.onEvent(EditProfileEvent.EnteredName(it)) }
            )
            Spacer(modifier = Modifier.height(24.dp))
            CustomTextField(
                text = state.username,
                fieldName = stringResource(R.string.username),
                hint = stringResource(R.string.hint_username),
                onTextChange = { viewModel.onEvent(EditProfileEvent.EnteredUsername(it)) }
            )
            Spacer(modifier = Modifier.height(24.dp))
            CustomTextField(
                text = state.email,
                fieldName = stringResource(R.string.email),
                hint = stringResource(R.string.hint_email),
                onTextChange = { viewModel.onEvent(EditProfileEvent.EnteredEmail(it)) }
            )
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 6.dp)
            ) {
                CustomButton(
                    text = stringResource(R.string.change),
                    onClick = { viewModel.onEvent(EditProfileEvent.SaveChanges) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}