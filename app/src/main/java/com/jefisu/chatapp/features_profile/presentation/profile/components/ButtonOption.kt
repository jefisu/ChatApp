package com.jefisu.chatapp.features_profile.presentation.profile.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jefisu.chatapp.features_chat.core.components.CustomIconButton

@Composable
fun ButtonOption(
    @DrawableRes icon: Int,
    text: String,
    onClick: () -> Unit,
    iconSize: Dp = 30.dp,
    textStyle: TextStyle = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        color = Color.White
    )
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        CustomIconButton(
            icon = ImageVector.vectorResource(icon),
            iconSize = iconSize,
            onClick = {}
        )
        Spacer(modifier = Modifier.width(36.dp))
        Text(
            text = text,
            style = textStyle
        )
    }
}