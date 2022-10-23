package com.jefisu.chatapp.features_chat.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jefisu.chatapp.R
import com.jefisu.chatapp.core.presentation.components.SearchTextField
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
import com.jefisu.chatapp.ui.theme.MineShaft
import com.jefisu.chatapp.ui.theme.Platinum
import com.jefisu.chatapp.ui.theme.SkyBlue
import com.jefisu.chatapp.ui.theme.Woodsmoke
import com.jefisu.chatapp.ui.theme.heightTopBar
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
    var isSelectionChatEnabled by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true, key2 = state?.selectedChats) {
        viewModel.getUsers()
        if (state?.selectedChats?.isEmpty() == true) {
            isSelectionChatEnabled = false
            viewModel.clearSelection()
        }
    }

    resultRecipient.onNavResult { result ->
        if (result is NavResult.Value && result.value.messages.isNotEmpty()) {
            viewModel.updateMessageByCurrentChat(result.value.chatId, result.value.messages)
        } else {
            viewModel.loadChats()
        }
    }

    if (showAlert) {
        AlertDialog(
            onDismissRequest = { showAlert = false },
            title = {
                Text(
                    text = if (state?.selectedChats?.size!! > 1) stringResource(
                        R.string.delete_several,
                        state?.selectedChats?.size!!,
                        "chats"
                    ) else stringResource(R.string.delete_title, "chat"),
                    fontSize = 20.sp
                )
            },
            text = {
                Text(
                    text = stringResource(
                        R.string.are_you_sure_you_want_to_delete,
                        if (state?.selectedChats?.size!! > 1) "these chats" else "this chat",
                    ),
                    fontSize = 18.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteChat()
                        showAlert = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text(text = stringResource(R.string.delete_button).uppercase())
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showAlert = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = SkyBlue)
                ) {
                    Text(text = stringResource(R.string.cancel_button).uppercase())
                }
            },
            backgroundColor = MineShaft,
            contentColor = Color.White,
            shape = RoundedCornerShape(8.dp)
        )
    }

    state?.let { _state ->
        Column(
            modifier = Modifier.background(Gunmetal)
        ) {
            AnimatedVisibility(visible = !isSearching && !isSelectionChatEnabled) {
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
                                text = _state.ownerUser?.name
                                    ?: _state.ownerUser?.username.orEmpty(),
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
                            avatarUrl = _state.ownerUser?.avatarUrl,
                            size = 47.dp,
                            onClick = {
                                navigator.navigate(ProfileScreenDestination())
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    LazyRow {
                        if (_state.ownerUser != null) {
                            item {
                                Spacer(modifier = Modifier.width(12.dp))
                                UserItem(
                                    user = _state.ownerUser,
                                    isFirstOwner = true,
                                    onClick = {}
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                            }
                        }
                        items(_state.users) { user ->
                            UserItem(
                                user = user,
                                onClick = {
                                    val existChat = _state.chats.find {
                                        it.recipientUser == user
                                    }
                                    navigator.navigate(
                                        ChatScreenDestination(
                                            chatId = existChat?.id,
                                            ownerId = _state.ownerUser?.id.orEmpty(),
                                            recipientUser = user,
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(heightTopBar)
                ) {
                    IconButton(
                        onClick = {
                            isSearching = !isSearching
                            viewModel.searchChats("")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = CoolGrey
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    SearchTextField(
                        text = _state.searchQuery,
                        onTextChange = viewModel::searchChats,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            AnimatedVisibility(visible = isSelectionChatEnabled) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    IconButton(onClick = {
                        isSelectionChatEnabled = false
                        viewModel.clearSelection()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear selection chats",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "${_state.selectedChats.size} selected",
                        fontSize = 22.sp,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Checkbox(
                        checked = _state.selectedChats.size == _state.chats.size,
                        onCheckedChange = { viewModel.selectAll() },
                        colors = CheckboxDefaults.colors(
                            uncheckedColor = Color.White
                        )
                    )
                    IconButton(onClick = { showAlert = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete chat",
                            tint = Color.White
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(Woodsmoke)
            ) {
                this@Column.AnimatedVisibility(
                    visible = _state.chats.isEmpty() && _state.searchQuery.isNotEmpty(),
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
                    items(_state.chats, key = { it.id }) { chat ->
                        chat.lastMessage?.let {
                            ChatItem(
                                chat = chat,
                                lastMessage = it,
                                isSelected = _state.selectedChats.contains(chat),
                                onClickImage = { /*TODO*/ },
                                modifier = Modifier
                                    .combinedClickable(
                                        onClick = {
                                            if (isSelectionChatEnabled) {
                                                viewModel.selectChat(chat)
                                                return@combinedClickable
                                            }
                                            navigator.navigate(
                                                ChatScreenDestination(
                                                    chatId = chat.id,
                                                    ownerId = _state.ownerUser?.id.orEmpty(),
                                                    recipientUser = chat.recipientUser,
                                                    messages = chat.messages
                                                )
                                            )
                                        },
                                        onLongClick = {
                                            if (!isSelectionChatEnabled) {
                                                isSelectionChatEnabled = true
                                                viewModel.selectChat(chat)
                                            }
                                        },
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = rememberRipple()
                                    )
                                    .padding(horizontal = 16.dp)
                                    .animateItemPlacement()
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}