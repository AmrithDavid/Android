// Type.kt
package com.singularhealth.android3dicom.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp

val provider =
    GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = emptyList(),
    )

val plusJakartaSans = GoogleFont("Plus Jakarta Sans")
val PlusJakartaSansFamily =
    FontFamily(
        Font(googleFont = plusJakartaSans, fontProvider = provider),
    )

val Typography =
    Typography(
        bodyLarge =
            TextStyle(
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 24.sp,

            ),
        titleLarge =
            TextStyle(
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.Normal, // changed from medium to normal despite wireframe info as it looks more similiar
                fontSize = 18.sp,
                lineHeight = 26.sp,
            ),
        labelSmall =
            TextStyle(
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.sp,
            ),
        titleMedium =
            TextStyle(
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.15.sp,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
        labelMedium =
            TextStyle(
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
            ),
        labelLarge =
            TextStyle(
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.1.sp,
            ),
        headlineMedium =
            TextStyle(
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
            ),
        displayLarge =
            TextStyle(
                fontFamily = PlusJakartaSansFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                lineHeight = 18.sp,
            )
    )
