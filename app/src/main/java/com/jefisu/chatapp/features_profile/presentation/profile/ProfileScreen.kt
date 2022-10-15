package com.jefisu.chatapp.features_profile.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jefisu.chatapp.R
import com.jefisu.chatapp.core.util.CustomTransitions
import com.jefisu.chatapp.core.util.UiEvent
import com.jefisu.chatapp.destinations.EditPasswordScreenDestination
import com.jefisu.chatapp.destinations.EditProfileScreenDestination
import com.jefisu.chatapp.features_chat.core.components.CustomIconButton
import com.jefisu.chatapp.features_chat.core.components.ProfileImage
import com.jefisu.chatapp.features_profile.presentation.profile.components.ButtonOption
import com.jefisu.chatapp.ui.theme.EbonyClay
import com.jefisu.chatapp.ui.theme.Gunmetal
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph
@Destination(style = CustomTransitions::class)
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()

    LaunchedEffect(
        key1 = viewModel.resultEvent,
        key2 = viewModel.user
    ) {
        viewModel.loadChangesUser()
        viewModel.resultEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    if (event.destination != null) {
                        navigator.navigate(event.destination)
                    } else {
                        navigator.navigateUp()
                    }
                }

                is UiEvent.ShowBottomSheet -> Unit
            }
        }
    }

    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(237.dp)
                .background(Gunmetal)
        )
        Column(
            modifier = Modifier.padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp
            )
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
            Spacer(modifier = Modifier.height(36.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(223.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(EbonyClay)
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProfileImage(
                        avatarUrl = user?.avatarUrl,
                        size = 120.dp
                    )
                    Text(
                        text = user?.name ?: user?.username.orEmpty(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Text(
                        text = "@${user?.username}",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
                IconButton(
                    onClick = { navigator.navigate(EditProfileScreenDestination(user!!)) },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_edit_user),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            ButtonOption(
                icon = R.drawable.ic_change_password,
                text = stringResource(R.string.change_password),
                onClick = {
                    navigator.navigate(EditPasswordScreenDestination(user?.username.orEmpty()))
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            ButtonOption(
                icon = R.drawable.ic_exit_account,
                text = stringResource(R.string.logout),
                onClick = viewModel::logout
            )
        }
    }
}