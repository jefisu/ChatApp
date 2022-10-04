package com.jefisu.chatapp.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jefisu.chatapp.ui.theme.VirginAmerica

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundBrush: Brush = Brush.linearGradient(VirginAmerica),
    height: Dp = 75.dp,
    width: Dp = 200.dp,
    shape: Shape = RoundedCornerShape(20.dp),
    textStyle: TextStyle = TextStyle(
        fontSize = 20.sp,
        color = Color.White
    ),
    isLoading: Boolean = false
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .defaultMinSize(width, height)
            .clip(shape)
            .background(backgroundBrush)
            .clickable(
                onClick = onClick,
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false)
            )
            .then(modifier)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White
            )
            return@Box
        }
        Text(
            text = text,
            style = textStyle
        )
    }
}