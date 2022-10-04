package com.jefisu.chatapp.features_chat.presentation.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jefisu.chatapp.R
import com.jefisu.chatapp.ui.theme.CharlestonGreen
import com.jefisu.chatapp.ui.theme.CoolGrey
import com.jefisu.chatapp.ui.theme.Gunmetal
import com.jefisu.chatapp.ui.theme.SkyBlue

@Composable
fun ChatBubble(
    message: String,
    time: String,
    isRead: Boolean,
    isOwnMessage: Boolean
) {
    Box(
        contentAlignment = if (isOwnMessage) Alignment.CenterEnd else Alignment.CenterStart,
        modifier = Modifier.fillMaxWidth()
    ) {
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