package com.jefisu.chatapp.features_chat.presentation.chat

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.text.style.TextAlign
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
import com.jefisu.chatapp.ui.theme.CoolGrey
import com.jefisu.chatapp.ui.theme.Gunmetal
import com.jefisu.chatapp.ui.theme.MineShaft
import com.jefisu.chatapp.ui.theme.OuterSpace
import com.jefisu.chatapp.ui.theme.QuickSilver
import com.jefisu.chatapp.ui.theme.SkyBlue
import com.jefisu.chatapp.ui.theme.Woodsmoke
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
                    text = if (state.selectedMessages.size > 1) stringResource(
                        R.string.delete_several,
                        state.selectedMessages.size,
                        "messages"
                    ) else stringResource(R.string.delete_title, "message"),
                    fontSize = 20.sp
                )
            },
            text = {
                Text(
                    text = stringResource(
                        R.string.are_you_sure_you_want_to_delete,
                        if (state.selectedMessages.size > 1) "these messages" else "this message"
                    ),
                    fontSize = 18.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onEvent(ChatEvent.DeleteMessages)
                        showAlert = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text(text = "DELETE")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showAlert = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = SkyBlue)
                ) {
                    Text(text = "CANCEL")
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
                    .padding(10.dp)
            ) {
                CustomIconButton(
                    icon = Icons.Default.ArrowBack,
                    onClick = {
                        resultBackNavigator.navigateBack(
                            HomeNavArg(state.chatId, state.messages as ArrayList<Message>)
                        )
                    }
                )
                Text(
                    text = state.recipientUser?.username.orEmpty(),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                ProfileImage(
                    avatarUrl = state.recipientUser?.avatarUrl,
                    size = 47.dp
                )
            }
        }
        AnimatedVisibility(visible = state.selectedMessages.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
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
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(Woodsmoke)
                .padding(bottom = 12.dp)
        ) {
            LazyColumn(
                reverseLayout = true,
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Bottom),
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
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Divider(
                                    color = CoolGrey,
                                    modifier = Modifier.width(80.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = DateUtil.getDateByMessages(message.timestamp),
                                    color = CoolGrey,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Divider(
                                    color = CoolGrey,
                                    modifier = Modifier.width(80.dp)
                                )
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
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