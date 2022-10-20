package com.jefisu.chatapp.features_chat.presentation.chat.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jefisu.chatapp.R
import com.jefisu.chatapp.core.presentation.animateAlignmentAsState
import com.jefisu.chatapp.ui.theme.CharlestonGreen
import com.jefisu.chatapp.ui.theme.CoolGrey
import com.jefisu.chatapp.ui.theme.Gunmetal
import com.jefisu.chatapp.ui.theme.SkyBlue

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ChatBubble(
    message: String,
    time: String,
    isRead: Boolean,
    isOwnMessage: Boolean,
    selectionEnabled: Boolean,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val selectedColorAnim by animateColorAsState(
        targetValue = if (isSelected) Color.White.copy(.4f) else Color.Transparent,
        animationSpec = tween(600)
    )
    val alignAnim by animateAlignmentAsState(if (selectionEnabled || isOwnMessage) Alignment.CenterEnd else Alignment.CenterStart)

    Box(
        modifier = Modifier
            .background(selectedColorAnim)
            .padding(vertical = if (selectionEnabled) 4.dp else 0.dp)
            .then(modifier)
    ) {
        AnimatedVisibility(
            visible = selectionEnabled,
            enter = slideInHorizontally { width -> -width * 3 },
            exit = slideOutHorizontally { width -> -width * 3 },
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .graphicsLayer {
                        translationX = -8.dp.toPx()
                        translationY = -2.dp.toPx()
                    }
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color.Green.copy(.2f) else Color.Transparent)
                    .border(
                        width = 1.dp,
                        color = Color.White,
                        shape = CircleShape
                    )
            ) {
                AnimatedVisibility(
                    visible = isSelected,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .heightIn(min = 35.dp)
                .widthIn(min = 100.dp, 300.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 15.dp,
                        topEnd = 15.dp,
                        bottomEnd = if (isOwnMessage) 0.dp else 15.dp,
                        bottomStart = if (!isOwnMessage) 0.dp else 15.dp
                    )
                )
                .background(if (isOwnMessage) Gunmetal else CharlestonGreen)
                .padding(horizontal = 6.dp, vertical = 4.dp)
                .align(alignAnim)
        ) {
            Text(
                text = message,
                color = Color.White,
                textAlign = TextAlign.Justify,
                lineHeight = 18.sp,
                maxLines = 7
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = time,
                    color = CoolGrey,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_done_double_checked),
                    contentDescription = null,
                    tint = if (isRead) SkyBlue else CoolGrey,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}