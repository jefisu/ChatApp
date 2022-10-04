package com.jefisu.chatapp.core.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jefisu.chatapp.R
import com.jefisu.chatapp.ui.theme.ChineseBlack
import com.jefisu.chatapp.ui.theme.CoolGrey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(
    state: ModalBottomSheetState,
    scope: CoroutineScope,
    error: String,
    backgroundColor: Color = ChineseBlack,
    shape: Shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    content: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
        sheetState = state,
        sheetBackgroundColor = backgroundColor,
        sheetShape = shape,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Divider(
                    color = CoolGrey,
                    thickness = 4.dp,
                    modifier = Modifier
                        .width(50.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                CustomButton(
                    text = stringResource(R.string.ok),
                    height = 40.dp,
                    shape = RoundedCornerShape(8.dp),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch { state.hide() }
                    }
                )
            }
        }
    ) {
        content()
    }
}