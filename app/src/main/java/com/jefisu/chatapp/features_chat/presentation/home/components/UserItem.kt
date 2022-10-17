package com.jefisu.chatapp.features_chat.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jefisu.chatapp.core.data.model.User
import com.jefisu.chatapp.features_chat.core.components.ProfileImage

@Composable
fun UserItem(
    user: User,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFirstOwner: Boolean = false,
    textSize: TextUnit = 14.sp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        ProfileImage(
            avatarUrl = user.avatarUrl,
            onClick = onClick,
            iconCorner = if (isFirstOwner) Icons.Default.Add else null
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (isFirstOwner) "Me" else user.username,
            fontSize = textSize,
            color = Color.White
        )
    }
}