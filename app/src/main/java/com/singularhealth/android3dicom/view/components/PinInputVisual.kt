package com.singularhealth.android3dicom.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
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
) {
    Box(
        modifier =
            Modifier
                .width(200.dp)
                .height(56.dp)
                .background(BackgroundGray, RoundedCornerShape(8.dp)),
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
                                if (index < pin.length) DarkBlue else Color.Transparent,
                                CircleShape,
                            ).border(2.dp, BorderColor, CircleShape),
                )
            }
        }
        BasicTextField(
            value = pin,
            onValueChange = { if (it.length <= 4) onPinChange(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.Center) {
                    innerTextField()
                }
            },
        )
    }
}
