package com.jefisu.chatapp.core.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.jefisu.chatapp.ui.theme.DarkLiver
import com.jefisu.chatapp.ui.theme.EerieBlack
import com.jefisu.chatapp.ui.theme.MaximumPurple
import com.jefisu.chatapp.ui.theme.SilverSand
import com.jefisu.chatapp.ui.theme.TaupeGray
import com.jefisu.chatapp.ui.theme.VampireBlack

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
            visualTransformation = if (showPassword) {
                VisualTransformation.None
            } else visualTransformation,
            trailingIcon = {
                if (isPassword && text.isNotBlank()) {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) {
                                Icons.Default.VisibilityOff
                            } else Icons.Default.Visibility,
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