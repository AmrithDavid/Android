@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.Android3DicomTheme
import com.singularhealth.android3dicom.view.components.ImageDetailOptionsMenu
import androidx.lifecycle.viewmodel.compose.viewModel
import com.singularhealth.android3dicom.viewmodel.ImageDetailViewModel

@Suppress("ktlint:standard:function-naming")
@Composable
fun MainImageMenu(
    viewModel: ImageDetailViewModel = viewModel(),
    navController: NavController,
) {
    var showDropdown by remember { mutableStateOf(false) }
    var selectedButton by remember { mutableStateOf("3D") }

    Android3DicomTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            MainImageMenuTopBar(
                selectedButton = selectedButton,
                onButtonSelected = { selectedButton = it },
                on3DClick = { viewModel.on3DClick() },
                onTransverseClick = { viewModel.onTransverseClick() },
                onSagittalClick = { viewModel.onSagittalClick() },
                onCoronalClick = { viewModel.onCoronalClick() },
                onMoreClick = { showDropdown = true },
                onBackClick = { navController.navigateUp() },
            )

            // Content area
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Text(
                        text = "Scan Image goes here",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                // Options menu
                Box(
                    modifier =
                        Modifier
                            .align(Alignment.TopEnd)
                            .offset(y = (-76).dp),
                ) {
                    ImageDetailOptionsMenu(
                        expanded = showDropdown,
                        onDismissRequest = { showDropdown = false },
                        onItemClick = { selectedOption ->
                            when (selectedOption) {
                                "Share" -> navController.navigate("shareView")
                                "More Info" -> { /* Handle More Info */ }
                                "Delete" -> { /* Handle Delete */ }
                                "Report" -> { /* Handle Report */ }
                                "Support" -> { /* Handle Support */ }
                            }
                            showDropdown = false
                        },
                    )
                }
            }

            MainImageMenuBottomBar()
        }
    }
}

@Composable
fun MainImageMenuTopBar(
    selectedButton: String,
    onButtonSelected: (String) -> Unit,
    on3DClick: () -> Unit,
    onTransverseClick: () -> Unit,
    onSagittalClick: () -> Unit,
    onCoronalClick: () -> Unit,
    onMoreClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

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
                    modifier =
                        Modifier
                            .size(24.dp)
                            .clickable { onBackClick() },
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
                modifier =
                    Modifier
                        .size(24.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { onMoreClick() },
            )
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
        ) {
            listOf("3D", "Transverse", "Sagittal", "Coronal").forEach { buttonText ->
                TopBarButton(
                    text = buttonText,
                    isSelected = selectedButton == buttonText,
                    onClick = {
                        onButtonSelected(buttonText)
                        // Call appropriate ViewModel method based on buttonText
                        when (buttonText) {
                            "3D" -> on3DClick()
                            "Transverse" -> onTransverseClick()
                            "Sagittal" -> onSagittalClick()
                            "Coronal" -> onCoronalClick()
                        }
                    },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
fun TopBarButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(36.dp),
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color =
                    if (isSelected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                    },
                modifier =
                    Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onClick,
                        ).wrapContentSize(Alignment.Center),
            )
        }

        // Drawing the line
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(if (isSelected) MaterialTheme.colorScheme.onPrimary else Color.Transparent),
        )
    }
}

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

@Preview(showBackground = true)
@Composable
fun MainImageMenuPreview() {
    Android3DicomTheme {
        //TODO: argument error below
        MainImageMenu(navController = rememberNavController())
    }
}
