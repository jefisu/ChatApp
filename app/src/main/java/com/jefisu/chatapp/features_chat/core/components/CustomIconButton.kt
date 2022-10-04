package com.jefisu.chatapp.features_chat.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jefisu.chatapp.ui.theme.BrightGray

@Composable
fun CustomIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    rotationZIcon: Dp = 0.dp,
    iconColor: Color = Color.Black,
    iconSize: Dp = 24.dp,
    backgroundSize: Dp = 47.dp,
    backgroundShape: Shape = CircleShape,
    backgroundColor: Color = BrightGray,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(backgroundSize)
            .clip(backgroundShape)
            .background(backgroundColor)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier
                .size(iconSize)
                .graphicsLayer {
                    rotationZ = rotationZIcon.toPx()
                }
        )
    }
}