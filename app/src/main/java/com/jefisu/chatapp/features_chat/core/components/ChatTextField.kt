package com.jefisu.chatapp.features_chat.core.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jefisu.chatapp.ui.theme.CoolGrey
import com.jefisu.chatapp.ui.theme.OuterSpace
import com.jefisu.chatapp.ui.theme.QuickSilver

@Composable
fun ChatTextField(
    text: String,
    onTextChange: (String) -> Unit,
    onClearText: () -> Unit,
    hint: String,
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
        trailingIcon = {
            if (text.isNotBlank()) {
                IconButton(onClick = onClearText) {
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