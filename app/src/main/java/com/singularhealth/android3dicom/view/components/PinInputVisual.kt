@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.singularhealth.android3dicom.ui.theme.BackgroundGray
import com.singularhealth.android3dicom.ui.theme.BorderColor
import com.singularhealth.android3dicom.ui.theme.DarkBlue

@Suppress("ktlint:standard:function-naming")
@Composable
fun PinInputVisual(
    pin: String,
    onPinChange: (String) -> Unit,
    isError: Boolean,
    enabled: Boolean,
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = pin, selection = TextRange(pin.length))) }
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier =
            Modifier
                .width(200.dp)
                .height(56.dp)
                .background(BackgroundGray, RoundedCornerShape(8.dp))
                .then(if (!enabled) Modifier.alpha(0.5f) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        ) {
            repeat(4) { index ->
                Box(
                    modifier =
                        Modifier
                            .size(26.dp)
                            .background(
                                when {
                                    isError -> Color.Transparent
                                    index < pin.length -> DarkBlue
                                    else -> Color.Transparent
                                },
                                CircleShape,
                            ).then(
                                when {
                                    isError -> Modifier.border(2.dp, Color(0xFFB21817), CircleShape)
                                    index >= pin.length -> Modifier.border(2.dp, BorderColor, CircleShape)
                                    else -> Modifier // No border for filled circles
                                },
                            ),
                )
            }
        }
        BasicTextField(
            value = textFieldValueState,
            onValueChange = {
                if (enabled && it.text.length <= 4) {
                    textFieldValueState = it.copy(selection = TextRange(it.text.length))
                    onPinChange(it.text)
                }
            },
            enabled = enabled,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .focusRequester(focusRequester),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.Center) {
                    innerTextField()
                }
            },
            textStyle =
                androidx.compose.ui.text
                    .TextStyle(color = Color.Transparent),
            cursorBrush =
                androidx.compose.ui.graphics
                    .SolidColor(Color.Transparent),
        )
    }

    LaunchedEffect(enabled) {
        if (enabled) {
            focusRequester.requestFocus()
        }
    }
}
