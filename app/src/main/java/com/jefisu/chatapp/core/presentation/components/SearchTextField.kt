package com.jefisu.chatapp.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.jefisu.chatapp.R
import com.jefisu.chatapp.ui.theme.CoolGrey

@Composable
fun SearchTextField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = stringResource(R.string.search),
    style: TextStyle = TextStyle(fontSize = 20.sp, color = CoolGrey),
    showAutomaticKeyboard: Boolean = true
) {
    val focusRequester = FocusRequester()
    LaunchedEffect(key1 = Unit) {
        if (showAutomaticKeyboard) {
            focusRequester.requestFocus()
        }
    }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
    ) {
        if (text.isBlank()) {
            Text(
                text = hint,
                style = style
            )
        }
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            textStyle = style.copy(color = Color.White),
            cursorBrush = SolidColor(Color.White),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(1f - .15f)
                .focusRequester(focusRequester)
        )
        if (text.isNotBlank()) {
            IconButton(
                onClick = { onTextChange("") },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear search query",
                    tint = style.color
                )
            }
        }
    }
}