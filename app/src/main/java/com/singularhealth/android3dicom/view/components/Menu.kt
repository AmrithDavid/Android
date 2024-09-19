package com.singularhealth.android3dicom.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.Android3DicomTheme
import com.singularhealth.android3dicom.ui.theme.DividerColor

@Composable
fun SideMenu(
    onCloseMenu: () -> Unit,
    onHomeClick: () -> Unit,
    onClearCacheClick: () -> Unit,
    onBiometricClick: () -> Unit,
    onAboutClick: () -> Unit,
    onSupportClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val menuWidth = (screenWidth * 0.7f).coerceAtMost(400.dp)

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(menuWidth)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Top section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo
                    Image(
                        painter = painterResource(id = R.drawable.img_logo_mod_01),
                        contentDescription = "3DICOM Logo",
                        modifier = Modifier
                            .width(180.dp)
                            .height(56.dp),
                        contentScale = ContentScale.Fit
                    )

                    // Close button
                    IconButton(onClick = onCloseMenu) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "Close Menu",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Top menu items
                MenuButton(
                    icon = R.drawable.ic_home,
                    text = "Home",
                    onClick = onHomeClick
                )
                Spacer(modifier = Modifier.height(16.dp))
                MenuButton(
                    icon = R.drawable.ic_cache,
                    text = "Clear Cache",
                    onClick = onClearCacheClick
                )
                Spacer(modifier = Modifier.height(16.dp))
                MenuButton(
                    icon = R.drawable.ic_fingerprint,
                    text = "Turn on Biometric",
                    onClick = onBiometricClick
                )
            }

            // Divider
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = DividerColor
            )

            // Bottom section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                MenuButton(
                    icon = R.drawable.ic_info,
                    text = "About 3Dicom",
                    onClick = onAboutClick
                )
                Spacer(modifier = Modifier.height(16.dp))
                MenuButton(
                    icon = R.drawable.ic_support,
                    text = "Customer Support",
                    onClick = onSupportClick
                )
                Spacer(modifier = Modifier.height(16.dp))
                MenuButton(
                    icon = R.drawable.ic_logout,
                    text = "Log out",
                    onClick = onLogoutClick
                )
            }
        }
    }
}

@Composable
private fun MenuButton(
    icon: Int,
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun SideMenuPreview() {
    Android3DicomTheme {
        SideMenu(
            onCloseMenu = {},
            onHomeClick = {},
            onClearCacheClick = {},
            onBiometricClick = {},
            onAboutClick = {},
            onSupportClick = {},
            onLogoutClick = {}
        )
    }
}
