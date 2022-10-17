package com.jefisu.chatapp.features_chat.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jefisu.chatapp.ui.theme.CoolGrey
import com.jefisu.chatapp.ui.theme.Platinum

@Composable
fun ProfileImage(
    avatarUrl: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    size: Dp = 65.dp,
    shape: Shape = CircleShape,
    padding: PaddingValues = PaddingValues(),
    iconCorner: ImageVector? = null,
    iconCornerSize: Dp = 22.dp,
    iconCornerBackground: Color = Color.Black,
    iconCornerBackgroundSize: Dp = iconCornerSize + 2.dp,
    iconCornerSpace: Dp = 2.dp
) {
    Box(modifier = modifier) {
        if (!avatarUrl.isNullOrBlank()) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(padding)
                    .size(size)
                    .clip(shape)
                    .clickable(
                        onClick = onClick,
                        role = Role.Image,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false)
                    )
            )
        } else {
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .size(size)
                    .clip(shape)
                    .background(Platinum)
                    .clickable(
                        onClick = onClick,
                        role = Role.Image,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false)
                    )
                    .then(modifier)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = CoolGrey,
                    modifier = Modifier.size(55.dp)
                )
            }
        }
        if (iconCorner != null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(iconCornerBackgroundSize)
                    .graphicsLayer {
                        translationX = iconCornerSpace.toPx()
                        translationY = iconCornerSpace.toPx()
                    }
                    .clip(shape)
                    .background(iconCornerBackground)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(
                    imageVector = iconCorner,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(iconCornerSize)
                )
            }
        }
    }
}