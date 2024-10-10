@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.*

@Suppress("ktlint:standard:function-naming")
@Composable
fun ScanLibraryMenu(
    onCloseMenu: () -> Unit,
    onHomeClick: () -> Unit,
    onClearCacheClick: () -> Unit,
    onBiometricClick: () -> Unit,
    onAboutClick: () -> Unit,
    onSupportClick: () -> Unit,
    onLogoutClick: () -> Unit,
    isBiometricEnabled: Boolean,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val menuWidth = (screenWidth * 0.7f).coerceAtMost(400.dp)

    Surface(
        modifier =
            Modifier
                .fillMaxHeight()
                .width(menuWidth),
        color = Color.White,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars),
        ) {
            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(16.dp),
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Logo
                    Image(
                        painter = painterResource(id = R.drawable.img_logo_no_title),
                        contentDescription = "3DICOM Logo",
                        modifier =
                            Modifier
                                .width(180.dp)
                                .height(56.dp),
                        contentScale = ContentScale.Fit,
                    )

                    // Close button
                    IconButton(onClick = onCloseMenu) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "Close Menu",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Menu buttons
                MenuButton(
                    icon = R.drawable.ic_home,
                    text = "Home",
                    onClick = onHomeClick,
                    isHomeButton = true,
                )
                Spacer(modifier = Modifier.height(16.dp))
                MenuButton(
                    icon = R.drawable.ic_cache,
                    text = "Clear Cache",
                    onClick = onClearCacheClick,
                )
                Spacer(modifier = Modifier.height(16.dp))
//                MenuButton(
//                    icon = R.drawable.ic_fingerprint,
//                    text = "Turn on Biometric",
//                    onClick = onBiometricClick,
//                )
                MenuButton(
                    icon = if (isBiometricEnabled) R.drawable.ic_fingerprint else R.drawable.ic_pin, // Toggle between fingerprint and pin icons
                    text = if (isBiometricEnabled) "Turn on Biometric" else "Turn on Pin", // Toggle text
                    onClick = onBiometricClick,
                )
            }

            // Divider
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = DividerColor,
            )

            Column(
                modifier =
                    Modifier
                        .padding(16.dp),
            ) {
                // Bottom menu items
                MenuButton(
                    icon = R.drawable.ic_info,
                    text = "About 3Dicom",
                    onClick = onAboutClick,
                )
                Spacer(modifier = Modifier.height(16.dp))
                MenuButton(
                    icon = R.drawable.ic_support,
                    text = "Customer Support",
                    onClick = onSupportClick,
                )
                Spacer(modifier = Modifier.height(16.dp))
                MenuButton(
                    icon = R.drawable.ic_logout,
                    text = "Log out",
                    onClick = onLogoutClick,
                )
            }
        }
    }
}

@Composable
private fun MenuButton(
    icon: Int,
    text: String,
    onClick: () -> Unit,
    isHomeButton: Boolean = false,
) {
    val backgroundColor = if (isHomeButton) HomeButtonBackground else Color.Transparent
    val contentColor = if (isHomeButton) DarkBlue else Color.Black
    val iconColor = if (isHomeButton) DarkBlue else SubheadingColor

    Button(
        onClick = onClick,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = contentColor,
            ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .height(48.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = iconColor,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor,
            )
        }
    }
}
