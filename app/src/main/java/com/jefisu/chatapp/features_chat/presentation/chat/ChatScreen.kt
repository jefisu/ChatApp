package com.jefisu.chatapp.features_chat.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.jefisu.chatapp.features_chat.presentation.chat.components.ChatBubble
import com.jefisu.chatapp.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph
@Destination(
    style = CustomTransitions::class,
    navArgsDelegate = ChatNavArgs::class
)
@Composable
fun ChatScreen(
    lifecycle: Lifecycle,
    navigator: DestinationsNavigator,
    viewModel: ChatViewModel = hiltViewModel()
) {
    DisposableEffect(key1 = lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.connectChat()
                viewModel.getChat()
            } else if (event == Lifecycle.Event.ON_STOP) {
                viewModel.exitChat()
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    val state = viewModel.state
    val messagesMap = state.messages.groupBy { DateUtil.getDateExt(it.timestamp) }

    Column(
        modifier = Modifier
            .background(Gunmetal)
            .padding(top = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            CustomIconButton(
                icon = Icons.Default.ArrowBack,
                onClick = navigator::navigateUp
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
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(Woodsmoke)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 12.dp
                )
        ) {
            LazyColumn(
                reverseLayout = true,
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Bottom),
                modifier = Modifier.weight(1f)
            ) {
                messagesMap.forEach { (_, messages) ->
                    items(messages) { message ->
                        val isOwnMessage = message.username == state.ownerUsername
                        ChatBubble(
                            message = message.text,
                            time = DateUtil.toHour(message.timestamp),
                            isRead = false,
                            isOwnMessage = isOwnMessage
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
                val isEmptyMessage = state.messageText.isBlank()
                ChatTextField(
                    text = state.messageText,
                    onTextChange = { viewModel.onEvent(ChatEvent.MessageTextChange(it)) },
                    hint = stringResource(R.string.type_a_message),
                    onClearText = { viewModel.onEvent(ChatEvent.ClearText) },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                CustomIconButton(
                    icon = if (isEmptyMessage) Icons.Default.AttachFile else Icons.Default.Send,
                    backgroundSize = 55.dp,
                    rotationZIcon = if (isEmptyMessage) (-18).dp else 0.dp,
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