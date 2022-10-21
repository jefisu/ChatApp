package com.jefisu.chatapp.features_chat.presentation.chat

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.jefisu.chatapp.R
import com.jefisu.chatapp.core.util.CustomTransitions
import com.jefisu.chatapp.features_chat.core.components.ChatTextField
import com.jefisu.chatapp.features_chat.core.components.CustomIconButton
import com.jefisu.chatapp.features_chat.core.components.ProfileImage
import com.jefisu.chatapp.features_chat.core.util.DateUtil
import com.jefisu.chatapp.features_chat.domain.model.Message
import com.jefisu.chatapp.features_chat.presentation.chat.components.ChatBubble
import com.jefisu.chatapp.features_chat.presentation.home.HomeNavArg
import com.jefisu.chatapp.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.result.ResultBackNavigator

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@RootNavGraph
@Destination(
    style = CustomTransitions::class,
    navArgsDelegate = ChatNavArgs::class
)
@Composable
fun ChatScreen(
    lifecycle: Lifecycle,
    resultBackNavigator: ResultBackNavigator<HomeNavArg>,
    viewModel: ChatViewModel = hiltViewModel()
) {
    DisposableEffect(key1 = lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> viewModel.connectChat()
                Lifecycle.Event.ON_STOP -> viewModel.exitChat()
                else -> Unit
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    val state by viewModel.state.collectAsState()
    val messagesMap = state.messages.groupBy { DateUtil.getDateExt(it.timestamp) }
    var showAlert by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = state.selectedMessages) {
        if (state.selectedMessages.isEmpty()) {
            viewModel.onEvent(ChatEvent.ClearSelectionMessages)
        }
    }

    if (showAlert) {
        AlertDialog(
            onDismissRequest = { showAlert = false },
            title = {
                Text(
                    text = when {
                        state.selectedMessages.size > 1 -> stringResource(
                            R.string.delete_several,
                            state.selectedMessages.size,
                            "messages"
                        )
                        state.selectedMessages.size == 1 -> stringResource(
                            R.string.delete_title,
                            "message"
                        )
                        else -> stringResource(R.string.clear_history)
                    },
                    fontSize = 20.sp
                )
            },
            text = {
                Text(
                    text = when {
                        state.selectedMessages.size > 1 -> stringResource(
                            R.string.are_you_sure_you_want_to_delete,
                            "these messages"
                        )
                        state.selectedMessages.size == 1 -> stringResource(
                            R.string.are_you_sure_you_want_to_delete,
                            "this message"
                        )
                        else -> stringResource(
                            R.string.are_you_sure_you_want_to_clear,
                            state.recipientUser?.username.orEmpty()
                        )
                    },
                    fontSize = 18.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (state.selectedMessages.isNotEmpty()) {
                            viewModel.onEvent(ChatEvent.DeleteMessages)
                        } else {
                            viewModel.onEvent(ChatEvent.ClearHistory)
                        }
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
    Column(
        modifier = Modifier.background(Gunmetal)
    ) {
        AnimatedVisibility(visible = state.selectedMessages.isEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 7.dp)
            ) {
                IconButton(
                    onClick = {
                        resultBackNavigator.navigateBack(
                            HomeNavArg(state.chatId, state.messages as ArrayList<Message>)
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                ProfileImage(
                    avatarUrl = state.recipientUser?.avatarUrl,
                    size = 50.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = state.recipientUser?.username.orEmpty(),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { /* TODO */ }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    offset = DpOffset(x = 205.dp, (-45).dp),
                    modifier = Modifier
                        .background(MineShaft)
                        .width(180.dp)
                ) {
                    DropdownMenuItem(
                        onClick = {
                            showAlert = true
                            showMenu = false
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.clear_history),
                            color = Color.White
                        )
                    }
                }
            }
        }
        AnimatedVisibility(visible = state.selectedMessages.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 7.dp)
            ) {
                IconButton(onClick = { viewModel.onEvent(ChatEvent.ClearSelectionMessages) }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                AnimatedContent(
                    targetState = state.selectedMessages.size,
                    transitionSpec = {
                        if (targetState > initialState) {
                            slideInVertically { it } + fadeIn() with slideOutVertically { -it } + fadeOut()
                        } else {
                            slideInVertically { -it } + fadeIn() with slideOutVertically { it } + fadeOut()
                        } using SizeTransform(
                            clip = false
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) { targetCount ->
                    Text(
                        text = "$targetCount",
                        fontSize = 22.sp,
                        color = Color.White
                    )
                }
                IconButton(onClick = { showAlert = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.background(Woodsmoke)
        ) {
            LazyColumn(
                reverseLayout = true,
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Bottom),
                modifier = Modifier.weight(1f)
            ) {
                messagesMap.forEach { (_, messages) ->
                    items(messages) { message ->
                        ChatBubble(
                            message = message.text,
                            time = DateUtil.toHour(message.timestamp),
                            isRead = false,
                            isOwnMessage = message.userId == state.ownerId,
                            selectionEnabled = state.selectedMessages.isNotEmpty(),
                            isSelected = state.selectedMessages.contains(message),
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = {
                                        if (state.selectedMessages.isNotEmpty()) {
                                            viewModel.onEvent(ChatEvent.SelectMessage(message))
                                        }
                                    },
                                    onLongClick = {
                                        if (state.selectedMessages.isEmpty()) {
                                            viewModel.onEvent(ChatEvent.SelectMessage(message))
                                        }
                                    }
                                )
                                .padding(horizontal = 16.dp)
                        )
                    }
                    item {
                        messages.firstOrNull()?.let { message ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(
                                    8.dp,
                                    Alignment.CenterHorizontally
                                )
                            ) {
                                Divider(
                                    color = CoolGrey,
                                    modifier = Modifier.width(80.dp)
                                )
                                Text(
                                    text = DateUtil.getDateByMessages(message.timestamp),
                                    color = CoolGrey,
                                    fontSize = 12.sp
                                )
                                Divider(
                                    color = CoolGrey,
                                    modifier = Modifier.width(80.dp)
                                )
                            }
                        }
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                ChatTextField(
                    text = state.messageText,
                    onTextChange = { viewModel.onEvent(ChatEvent.MessageTextChange(it)) },
                    hint = stringResource(R.string.type_a_message),
                    onClearText = { viewModel.onEvent(ChatEvent.ClearText) },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                CustomIconButton(
                    icon = if (state.isBlankText) Icons.Default.AttachFile else Icons.Default.Send,
                    backgroundSize = 55.dp,
                    rotationZIcon = if (state.isBlankText) (-18).dp else 0.dp,
                    iconColor = QuickSilver,
                    backgroundColor = OuterSpace,
                    onClick = {
                        viewModel.onEvent(ChatEvent.SendMessage)
                    }
                )
            }
        }
    }
}