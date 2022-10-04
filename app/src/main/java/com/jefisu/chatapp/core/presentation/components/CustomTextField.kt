package com.jefisu.chatapp.core.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jefisu.chatapp.ui.theme.*

@Composable
fun CustomTextField(
    text: String,
    fieldName: String,
    hint: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    textSize: TextUnit = 16.sp,
    height: Dp = 65.dp,
    borderWidth: Dp = 2.dp,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        backgroundColor = VampireBlack,
        placeholderColor = DarkLiver,
        textColor = SilverSand,
        cursorColor = SilverSand
    ),
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    var hasFocus by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    Column {
        Text(
            text = fieldName,
            fontSize = textSize,
            color = TaupeGray,
            modifier = Modifier.padding(start = 12.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            shape = shape,
            singleLine = true,
            textStyle = TextStyle(fontSize = textSize),
            colors = colors,
            placeholder = { Text(text = hint) },
            keyboardOptions = keyboardOptions,
            visualTransformation = if (showPassword) VisualTransformation.None else visualTransformation,
            trailingIcon = {
                if (isPassword && text.isNotBlank()) {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .border(
                    width = borderWidth,
                    color = if (hasFocus) MaximumPurple else EerieBlack,
                    shape = shape
                )
                .onFocusEvent { hasFocus = it.hasFocus }
                .then(modifier)
        )
    }
}