@file:Suppress("ktlint:standard:no-wildcard-imports")

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.Android3DicomTheme

@Suppress("ktlint:standard:function-naming")
@Composable
fun MainImageMenu() {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        MainImageMenuTopBar()

        // Content area
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Scan Image goes here",
                style = MaterialTheme.typography.titleMedium,
            )
        }

        MainImageMenuBottomBar()
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun MainImageMenuTopBar() {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    var selectedButton by remember { mutableStateOf("3D") }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(top = statusBarHeight),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Back icon and title
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp),
                )

                Spacer(modifier = Modifier.width(27.dp))

                Text(
                    text = "Image title.3vxl",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            // More options icon
            Icon(
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = "More options",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(24.dp),
            )
        }

        // New row for the buttons
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TopBarButton(
                text = "3D",
                isSelected = selectedButton == "3D",
                onClick = { selectedButton = "3D" },
            )
            TopBarButton(
                text = "Transverse",
                isSelected = selectedButton == "Transverse",
                onClick = { selectedButton = "Transverse" },
            )
            TopBarButton(
                text = "Sagittal",
                isSelected = selectedButton == "Sagittal",
                onClick = { selectedButton = "Sagittal" },
            )
            TopBarButton(
                text = "Coronal",
                isSelected = selectedButton == "Coronal",
                onClick = { selectedButton = "Coronal" },
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun TopBarButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val contentColor =
        if (isSelected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
        }

    TextButton(
        onClick = onClick,
        colors =
            ButtonDefaults.textButtonColors(
                contentColor = contentColor,
            ),
        modifier =
            Modifier
                .height(36.dp)
                .drawBehind {
                    if (isSelected) {
                        val strokeWidth = 2.dp.toPx()
                        val y = size.height - strokeWidth / 2
                        drawLine(
                            color = Color.White,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidth,
                        )
                    }
                },
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun MainImageMenuBottomBar() {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            BottomBarButton(icon = R.drawable.ic_display, label = "Display")
            BottomBarButton(icon = R.drawable.ic_windowing, label = "Windowing")
            BottomBarButton(icon = R.drawable.ic_slicer, label = "Slicer")
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun BottomBarButton(
    icon: Int,
    label: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 22.dp),
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            modifier = Modifier.size(24.dp),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun MainImageMenuPreview() {
    Android3DicomTheme {
        MainImageMenu()
    }
}
