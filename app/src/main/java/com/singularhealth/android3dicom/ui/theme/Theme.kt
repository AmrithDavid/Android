// Theme.kt
package com.singularhealth.android3dicom.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme =
    lightColorScheme(
        primary = DarkBlue,
        secondary = PurpleGrey40,
        tertiary = Pink40,
        background = White,
        surface = White,
        onPrimary = White,
        onSecondary = White,
        onTertiary = White,
        onBackground = TitleColor,
        onSurface = TitleColor,
    )

@Suppress("ktlint:standard:function-naming")
@Composable
fun Android3DicomTheme(content: @Composable () -> Unit) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
