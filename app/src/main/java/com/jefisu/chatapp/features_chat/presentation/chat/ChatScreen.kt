package com.jefisu.chatapp.features_chat.presentation.chat

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.jefisu.chatapp.R
import com.jefisu.chatapp.core.presentation.components.SearchTextField
import com.jefisu.chatapp.core.util.CustomTransitions
import com.jefisu.chatapp.core.util.indexesOf
import com.jefisu.chatapp.core.util.quantityMessagesLocated
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
import com.jefisu.chatapp.ui.theme.heightTopBar
import com.maxkeppeker.sheets.core.icons.LibIcons
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.emoji.EmojiDialog
import com.maxkeppeler.sheets.emoji.models.EmojiConfig
import com.maxkeppeler.sheets.emoji.models.EmojiSelection
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
    var isSearching by remember { mutableStateOf(false) }
    val iconRotateAnim by animateFloatAsState(
        targetValue = if (state.selectedMessages.isNotEmpty()) 90f else 0f,
        animationSpec = tween(easing = LinearEasing)
    )
    val bottomPaddingAnim by animateDpAsState(
        targetValue = (if (!isSearching) 8 else 0).dp,
        animationSpec = tween(100)
    )
    val bottomHeightAnim by animateDpAsState(
        targetValue = (if (!isSearching) 64 else 56).dp,
        animationSpec = tween(100)
    )
    val quantityMessagesLocated = state.messages.quantityMessagesLocated(state.searchMessageQuery)
    val indicesMessagesLocated = state.messages.indexesOf(state.searchMessageQuery)
    var currentSearchIndex by remember { mutableStateOf(0) }
    val contentColor = LocalContentColor.current
    val emojiState = rememberSheetState()

    LaunchedEffect(key1 = state.selectedMessages, key2 = state.searchMessageQuery) {
        if (state.selectedMessages.isEmpty()) {
            viewModel.onEvent(ChatEvent.ClearSelectionMessages)
        }
        if (state.searchMessageQuery.isBlank()) {
            currentSearchIndex = 0
        }
    }

    EmojiDialog(
        state = emojiState,
        selection = EmojiSelection.Unicode(
            withButtonView = true,
            onPositiveClick = { emojiUnicode ->
                viewModel.onEvent(ChatEvent.MessageTextChange(emojiUnicode))
            }
        ),
        config = EmojiConfig(icons = LibIcons.TwoTone)
    )
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(heightTopBar)
                .padding(vertical = 6.dp)
        ) {
            IconButton(
                onClick = {
                    when {
                        state.selectedMessages.isNotEmpty() -> {
                            viewModel.onEvent(ChatEvent.ClearSelectionMessages)
                        }
                        isSearching -> {
                            isSearching = false
                            viewModel.onEvent(ChatEvent.SearchMessage(""))
                        }

                        else -> {
                            resultBackNavigator.navigateBack(
                                HomeNavArg(state.chatId, state.messages as ArrayList<Message>)
                            )
                        }
                    }
                },
                modifier = Modifier.graphicsLayer(rotationZ = iconRotateAnim)
            ) {
                Icon(
                    imageVector = if (state.selectedMessages.isNotEmpty()) {
                        Icons.Default.Close
                    } else Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box {
                this@Row.AnimatedVisibility(
                    visible = state.selectedMessages.isEmpty() && !isSearching,
                    enter = fadeIn(tween(200)),
                    exit = fadeOut(tween(200))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
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
                        IconButton(onClick = { isSearching = !isSearching }) {
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
                        AnimatedVisibility(
                            visible = showMenu,
                            modifier = Modifier.offset(x = (-5).dp, y = (-25).dp)
                        ) {
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false },
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
                }
                when {
                    isSearching -> {
                        SearchTextField(
                            text = state.searchMessageQuery,
                            onTextChange = { viewModel.onEvent(ChatEvent.SearchMessage(it)) },
                            showAutomaticKeyboard = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterStart)
                        )
                    }

                    state.selectedMessages.isNotEmpty() -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AnimatedContent(
                                targetState = state.selectedMessages.size,
                                transitionSpec = {
                                    if (targetState > initialState) {
                                        slideInVertically(
                                            initialOffsetY = { it }
                                        ) + fadeIn() with slideOutVertically(
                                            targetOffsetY = { -it }
                                        ) + fadeOut()
                                    } else {
                                        slideInVertically(
                                            initialOffsetY = { -it }
                                        ) + fadeIn() with slideOutVertically(
                                            targetOffsetY = { it }
                                        ) + fadeOut()
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
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.background(Woodsmoke)
        ) {
            LazyColumn(
                reverseLayout = true,
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Bottom),
                modifier = Modifier.weight(1f)
            ) {
                messagesMap.forEach { (_, messages) ->
                    itemsIndexed(messages) { index, message ->
                        val currentSelectedIndex = indicesMessagesLocated[currentSearchIndex]
                        val locatedText = if (currentSelectedIndex == index) {
                            state.searchMessageQuery
                        } else ""
                        ChatBubble(
                            message = message.text,
                            time = DateUtil.toHour(message.timestamp),
                            isRead = false,
                            isOwnMessage = message.userId == state.ownerId,
                            selectionEnabled = state.selectedMessages.isNotEmpty(),
                            isSelected = state.selectedMessages.contains(message),
                            findText = locatedText,
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = {
                                        if (state.selectedMessages.isNotEmpty() && !isSearching) {
                                            viewModel.onEvent(ChatEvent.SelectMessage(message))
                                        }
                                    },
                                    onLongClick = {
                                        if (state.selectedMessages.isEmpty() && !isSearching) {
                                            viewModel.onEvent(ChatEvent.SelectMessage(message))
                                        }
                                    }
                                )
                                .padding(horizontal = 8.dp)
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
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(bottomHeightAnim)
                    .background(if (isSearching) OuterSpace else Color.Transparent)
                    .padding(horizontal = bottomPaddingAnim)
                    .padding(bottom = bottomPaddingAnim)
            ) {
                if (!isSearching) {
                    ChatTextField(
                        text = state.messageText,
                        onTextChange = { viewModel.onEvent(ChatEvent.MessageTextChange(it)) },
                        hint = stringResource(R.string.type_a_message),
                        showEmojis = { emojiState.show() },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    CustomIconButton(
                        icon = if (state.isBlankText) {
                            Icons.Default.AttachFile
                        } else Icons.Default.Send,
                        backgroundSize = 55.dp,
                        rotationZIcon = if (state.isBlankText) (-18).dp else 0.dp,
                        iconColor = QuickSilver,
                        backgroundColor = OuterSpace,
                        onClick = {
                            viewModel.onEvent(ChatEvent.SendMessage)
                        }
                    )
                } else {
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "${currentSearchIndex + 1} of $quantityMessagesLocated",
                        color = SkyBlue,
                        fontSize = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        if (currentSearchIndex < quantityMessagesLocated - 1) {
                            currentSearchIndex += 1
                        }
                    }) {
                        val color = if (currentSearchIndex < quantityMessagesLocated - 1) {
                            CoolGrey
                        } else contentColor.copy(alpha = ContentAlpha.disabled)
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = null,
                            tint = color
                        )
                    }
                    IconButton(onClick = {
                        if (currentSearchIndex != 0) {
                            currentSearchIndex -= 1
                        }
                    }) {
                        val color = if (currentSearchIndex > 0) CoolGrey else {
                            contentColor.copy(alpha = ContentAlpha.disabled)
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = null,
                            tint = color
                        )
                    }
                }
            }
        }
    }
}