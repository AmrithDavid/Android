package com.singularhealth.android3dicom.view.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Suppress("ktlint:standard:function-naming")
@Composable
fun LoadingSpinner(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
            ),
    )
    Canvas(modifier = modifier.size(48.dp)) {
        val strokeWidth = 4.dp.toPx()
        val center = Offset(size.width / 2, size.height / 2)
        val radius = (size.width - strokeWidth) / 2
        val sweepAngle = 300f
        val startAngle = angle - 90f - sweepAngle / 2
        drawArc(
            color = Color(0xFF50A5DE),
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
            size = Size(size.width - strokeWidth, size.height - strokeWidth),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
        )
    }
}
