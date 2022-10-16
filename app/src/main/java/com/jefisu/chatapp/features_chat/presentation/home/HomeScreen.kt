package com.jefisu.chatapp.features_chat.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jefisu.chatapp.R
import com.jefisu.chatapp.core.util.CustomTransitions
import com.jefisu.chatapp.destinations.ChatScreenDestination
import com.jefisu.chatapp.destinations.ProfileScreenDestination
import com.jefisu.chatapp.features_chat.core.components.CustomIconButton
import com.jefisu.chatapp.features_chat.core.components.ProfileImage
import com.jefisu.chatapp.features_chat.core.util.DateUtil
import com.jefisu.chatapp.features_chat.presentation.home.components.ChatItem
import com.jefisu.chatapp.features_chat.presentation.home.components.UserItem
import com.jefisu.chatapp.ui.theme.CoolGrey
import com.jefisu.chatapp.ui.theme.Gunmetal
import com.jefisu.chatapp.ui.theme.Platinum
import com.jefisu.chatapp.ui.theme.Woodsmoke
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient

@OptIn(ExperimentalFoundationApi::class)
@RootNavGraph
@Destination(style = CustomTransitions::class)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    resultRecipient: ResultRecipient<ChatScreenDestination, HomeNavArg>,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var isSearching by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.getUsers()
    }

    resultRecipient.onNavResult { result ->
        if (result is NavResult.Value) {
            viewModel.updateMessageByCurrentChat(result.value.chatId, result.value.messages)
        }
    }

    state?.let { _state ->
        val ownerUser = _state.ownerUser
        val chats = _state.chats
        val users = _state.users
        val searchQuery = _state.searchQuery

        Column(
            modifier = Modifier.background(Gunmetal)
        ) {
            AnimatedVisibility(visible = !isSearching) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .padding(top = 12.dp)
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
                                text = ownerUser?.name ?: ownerUser?.username.orEmpty(),
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        CustomIconButton(
                            icon = Icons.Default.Search,
                            onClick = { isSearching = !isSearching }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        ProfileImage(
                            avatarUrl = ownerUser?.avatarUrl,
                            size = 47.dp,
                            onClick = {
                                navigator.navigate(ProfileScreenDestination())
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    LazyRow {
                        if (ownerUser != null) {
                            item {
                                Spacer(modifier = Modifier.width(12.dp))
                                UserItem(
                                    user = ownerUser,
                                    isFirstOwner = true,
                                    onClick = {}
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                            }
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
                                            ownerUsername = ownerUser?.username.orEmpty(),
                                            recipientUsername = user.username,
                                            recipientAvatarUrl = user.avatarUrl,
                                            messages = existChat?.messages ?: arrayListOf()
                                        )
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            AnimatedVisibility(visible = isSearching) {
                TextField(
                    value = searchQuery,
                    onValueChange = viewModel::searchChats,
                    singleLine = true,
                    placeholder = { Text(text = "Search...", fontSize = 20.sp) },
                    textStyle = TextStyle(fontSize = 20.sp),
                    leadingIcon = {
                        IconButton(onClick = {
                            isSearching = !isSearching
                            viewModel.searchChats("")
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Close search",
                                tint = CoolGrey
                            )
                        }
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.searchChats("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear search query",
                                    tint = CoolGrey
                                )
                            }
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.White,
                        textColor = Color.White,
                        placeholderColor = CoolGrey,
                        backgroundColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(Woodsmoke)
            ) {
                this@Column.AnimatedVisibility(
                    visible = chats.isEmpty() && searchQuery.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(
                        text = stringResource(R.string.no_results_found),
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
                LazyColumn {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    items(chats, key = { it.id }) { chat ->
                        chat.lastMessage?.let {
                            ChatItem(
                                chat = chat,
                                lastMessage = it,
                                onNavigateClick = {
                                    navigator.navigate(
                                        ChatScreenDestination(
                                            chatId = chat.id,
                                            ownerUsername = ownerUser?.username.orEmpty(),
                                            recipientUsername = chat.recipientUser.username,
                                            recipientAvatarUrl = chat.recipientUser.avatarUrl,
                                            messages = chat.messages
                                        )
                                    )
                                },
                                onClickImage = { /*TODO*/ },
                                modifier = Modifier.animateItemPlacement()
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}