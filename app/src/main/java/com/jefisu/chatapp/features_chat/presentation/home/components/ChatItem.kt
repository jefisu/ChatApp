package com.jefisu.chatapp.features_chat.presentation.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jefisu.chatapp.R
import com.jefisu.chatapp.features_chat.core.components.ProfileImage
import com.jefisu.chatapp.features_chat.domain.model.Message
import com.jefisu.chatapp.features_chat.presentation.home.ChatPreview
import com.jefisu.chatapp.ui.theme.DimGray
import com.jefisu.chatapp.ui.theme.Platinum
import com.jefisu.chatapp.ui.theme.SkyBlue

@Composable
fun ChatItem(
    chat: ChatPreview,
    lastMessage: Message,
    onClickImage: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    val alpha by animateFloatAsState(
        targetValue = if (isSelected) 180f else 0f,
        animationSpec = tween(600)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ProfileImage(
            avatarUrl = if (isSelected) null else chat.recipientUser?.avatarUrl,
            iconAvatar = if (isSelected) Icons.Default.Done else Icons.Default.Person,
            iconAvatarAlignment = if (isSelected) Alignment.Center else Alignment.BottomCenter,
            onClick = onClickImage,
            modifier = Modifier.graphicsLayer {
                rotationY = alpha
            }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chat.recipientUser?.username.orEmpty(),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = chat.timeLastMessage,
                    color = Platinum,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = lastMessage.text,
                    fontSize = 16.sp,
                    color = DimGray,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                if (chat.ownerSentLastMessage) {
                    Icon(
                        painter = painterResource(R.drawable.ic_done_double_checked),
                        contentDescription = null,
                        tint = DimGray
                    )
                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(25.dp)
                            .height(17.dp)
                            .clip(RoundedCornerShape(8.5.dp))
                            .background(SkyBlue)
                    ) {
                        Text(
                            text = "0",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}