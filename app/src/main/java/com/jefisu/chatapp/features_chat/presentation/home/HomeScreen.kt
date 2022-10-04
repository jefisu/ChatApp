package com.jefisu.chatapp.features_chat.presentation.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.core.util.CustomTransitions
import com.jefisu.chatapp.destinations.ChatScreenDestination
import com.jefisu.chatapp.destinations.ProfileScreenDestination
import com.jefisu.chatapp.features_chat.core.components.CustomIconButton
import com.jefisu.chatapp.features_chat.core.components.ProfileImage
import com.jefisu.chatapp.features_chat.core.util.DateUtil
import com.jefisu.chatapp.features_chat.presentation.home.components.ChatItem
import com.jefisu.chatapp.features_chat.presentation.home.components.UserItem
import com.jefisu.chatapp.ui.theme.Gunmetal
import com.jefisu.chatapp.ui.theme.Platinum
import com.jefisu.chatapp.ui.theme.Woodsmoke
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph
@Destination(
    style = CustomTransitions::class,
    navArgsDelegate = User::class
)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.getUser()
    }

    state?.let { s ->
        val ownerUser = s.ownerUser
        val chats = s.chats
        val users = s.users

        Column(
            modifier = Modifier
                .background(Gunmetal)
                .padding(top = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = DateUtil.timeOfDay(),
                        fontSize = 16.sp,
                        color = Platinum
                    )
                    Text(
                        text = ownerUser.name ?: ownerUser.username,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                CustomIconButton(
                    icon = Icons.Default.Search,
                    onClick = {
                        Toast.makeText(context, "Is not implemented", Toast.LENGTH_SHORT).show()
                    }
                )
                Spacer(modifier = Modifier.width(12.dp))
                ProfileImage(
                    avatarUrl = ownerUser.avatarUrl,
                    size = 47.dp,
                    onClick = {
                        navigator.navigate(ProfileScreenDestination(ownerUser))
                    }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            LazyRow {
                item {
                    Spacer(modifier = Modifier.width(12.dp))
                    UserItem(
                        user = ownerUser,
                        isFirstOwner = true,
                        onClick = {}
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
                items(users) { user ->
                    UserItem(
                        user = user,
                        onClick = {
                            val existChat = chats.find {
                                it.recipientUser == user
                            }
                            navigator.navigate(
                                ChatScreenDestination(
                                    chatId = existChat?.id,
                                    ownerUsername = ownerUser.username,
                                    recipientUsername = user.username
                                )
                            )
                        }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(Woodsmoke)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                items(chats) { chat ->
                    chat.lastMessage?.let {
                        ChatItem(
                            chat = chat,
                            lastMessage = it,
                            onNavigateClick = {
                                navigator.navigate(
                                    ChatScreenDestination(
                                        chat.id,
                                        ownerUser.username,
                                        chat.recipientUser.username
                                    )
                                )
                            },
                            onClickImage = { /*TODO*/ }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}