package com.jefisu.chatapp.features_chat.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jefisu.chatapp.R
import com.jefisu.chatapp.ui.theme.CoolGrey
import com.jefisu.chatapp.ui.theme.OuterSpace
import com.jefisu.chatapp.ui.theme.QuickSilver

@Composable
fun ChatTextField(
    text: String,
    onTextChange: (String) -> Unit,
    hint: String,
    showEmojis: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    textStyle: TextStyle = TextStyle(fontSize = 16.sp),
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        backgroundColor = OuterSpace,
        placeholderColor = QuickSilver,
        textColor = Color.White,
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
        cursorColor = Color.White
    )
) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        shape = shape,
        textStyle = textStyle,
        placeholder = { Text(text = hint) },
        colors = colors,
        leadingIcon = {
            Image(
                painter = painterResource(R.drawable.ic_emoji),
                contentDescription = null,
                colorFilter = ColorFilter.tint(CoolGrey),
                modifier = Modifier.clickable(onClick = showEmojis)
            )
        },
        trailingIcon = {
            if (text.isNotBlank()) {
                IconButton(onClick = { onTextChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = CoolGrey
                    )
                }
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}