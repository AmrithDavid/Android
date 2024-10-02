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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.Android3DicomTheme
import com.singularhealth.android3dicom.view.components.ImageDetailOptionsMenu
import com.singularhealth.android3dicom.view.components.LoadingSpinner
import com.singularhealth.android3dicom.viewmodel.ImageDetailViewModel

@Suppress("ktlint:standard:function-naming")
@Composable
fun ImageDetailView(
    viewModel: ImageDetailViewModel = viewModel(),
    navController: NavController,
) {
    var showDropdown by remember { mutableStateOf(false) }
    var selectedButton by remember { mutableStateOf("3D") }
    var showDisplayUI by remember {mutableStateOf(false)}
    var showWindowingUI by remember {mutableStateOf(false)}
    var showSlicerUI by remember {mutableStateOf(false)}

    // Shared slider state variables for Display UI
    var displayBrightness by remember { mutableStateOf(0.5f) }
    var displayContrast by remember { mutableStateOf(0.5f) }
    var displayOpacity by remember { mutableStateOf(0.5f) }

    // Shared slider state variables for Windowing and Slicer UIs
    var windowingRange by remember { mutableStateOf(30f..70f) }

    var slicerTransverse by remember { mutableStateOf(30f..70f) }
    var slicerSagittal by remember { mutableStateOf(30f..70f) }
    var slicerCoronal by remember { mutableStateOf(30f..70f) }

    //only one UI is displayed at a time
    var currentView by remember { mutableStateOf("None")}
    val isInitialLoading by viewModel.isInitialLoading.collectAsState()

    Android3DicomTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            ImageDetailTopBar(
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
                modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 0.dp),
            ) {
                if (isInitialLoading) {
                    LoadingSpinner(
                        modifier = Modifier.align(Alignment.Center),
                    )
                } else {
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

                // Conditionally display the Display UI
                when (currentView) {
                    "Display" -> DisplayUI(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .offset(y = 16.dp)
                    )
                    "Windowing" -> WindowingUI(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .offset(y = 16.dp)
                    )
                    "Slicer" -> SlicerUI(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .offset(y = 16.dp)
                    )
                    // No view selected, so nothing is displayed
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
            }

            ImageDetailBottomBar(
                onDisplayClick = {
                    // Toggle to Display view or none
                    currentView = if (currentView == "Display") "None" else "Display"
                },
                onWindowingClick = {
                    // Toggle to Windowing view or none
                    currentView = if (currentView == "Windowing") "None" else "Windowing"
                },
                onSlicerClick = {
                    // Toggle to Slicer view or none
                    currentView = if (currentView == "Slicer") "None" else "Slicer"
                }
            )
        }
    }
}

@Composable
fun ImageDetailTopBar(
    selectedButton: String,
    onButtonSelected: (String) -> Unit,
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
                    onClick = { onButtonSelected(buttonText) },
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
fun ImageDetailBottomBar(
    onDisplayClick: () -> Unit,
    onWindowingClick: () -> Unit,
    onSlicerClick: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            BottomBarButton(
                icon = R.drawable.ic_display,
                label = "Display",
                clickAction = {
                    // Action to perform when button is clicked
                    println("Display button clicked!")
                    onDisplayClick()
                })
            BottomBarButton(
                icon = R.drawable.ic_windowing,
                label = "Windowing",
                clickAction = {
                    // Action to perform when button is clicked
                    println("Windowing button clicked!")
                    onWindowingClick()
                })
            BottomBarButton(
                icon = R.drawable.ic_slicer,
                label = "Slicer",
                clickAction = {
                    // Action to perform when button is clicked
                    println("Slicer button clicked!")
                    onSlicerClick()
                })
        }
    }
}

@Composable
fun DisplayUI(modifier: Modifier = Modifier) {
    // interactive UI displayed when the "Display" button is clicked
    var selectedSetting by remember { mutableStateOf("Brightness") } // Default to Brightness
    var brightnessValue by remember { mutableStateOf(0.5f) } // Initial value for the slider
    var contrastValue by remember { mutableStateOf(0.5f) }
    var opacityValue by remember { mutableStateOf(0.5f) }

    // Main Box for Display UI
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color(0xFFFFFFFF))
            .padding(16.dp)
    ) {
        // Row with three buttons for Brightness, Contrast, and Opacity
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ButtonOption(
                icon = R.drawable.ic_brightness, // Placeholder icon for Brightness
                text = "Brightness",
                isSelected = selectedSetting == "Brightness",
                onClick = { selectedSetting = "Brightness" }
            )
            ButtonOption(
                icon = R.drawable.ic_contrast, // Placeholder icon for Contrast
                text = "Contrast",
                isSelected = selectedSetting == "Contrast",
                onClick = { selectedSetting = "Contrast" }
            )
            ButtonOption(
                icon = R.drawable.ic_opacity, // Placeholder icon for Opacity
                text = "Opacity",
                isSelected = selectedSetting == "Opacity",
                onClick = { selectedSetting = "Opacity" }
            )
        }

        // Conditionally display the slider based on the selected setting
        when (selectedSetting) {
            "Brightness" -> {
                // Brightness Slider
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center // Centering the Brightness Slider
                ) {
                    Slider(
                        value = brightnessValue,
                        onValueChange = { brightnessValue = it },
                        modifier = Modifier.width(360.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF50A5DE),
                            activeTrackColor = Color(0xFF50A5DE),
                            inactiveTrackColor = Color(0xFF50A5DE).copy(alpha = 0.5f)
                        )

                    )
                }
            }
            "Contrast" -> {
                // Contrast Slider
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center // Centering the Brightness Slider
                ) {
                    Slider(
                        value = contrastValue,
                        onValueChange = { contrastValue = it },
                        modifier = Modifier.width(360.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF50A5DE),
                            activeTrackColor = Color(0xFF50A5DE),
                            inactiveTrackColor = Color(0xFF50A5DE).copy(alpha = 0.5f)
                        )
                    )
                }
            }
            "Opacity" -> {
                // Opacity Slider
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center // Centering the Brightness Slider
                ) {
                    Slider(
                        value = opacityValue,
                        onValueChange = { opacityValue = it },
                        modifier = Modifier.width(360.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF50A5DE),
                            activeTrackColor = Color(0xFF50A5DE),
                            inactiveTrackColor = Color(0xFF50A5DE).copy(alpha = 0.5f)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ButtonOption(
    icon: Int,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFFE0E7FF) else Color.Transparent,
            contentColor = Color(0xFFF0F1FF)
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp), // Reduced padding inside the button
        modifier = Modifier.padding(horizontal = 4.dp) // Reduced padding between the buttons
//        modifier = Modifier.padding(horizontal = 7.dp)
    ) {
        Icon(
            painter = painterResource(id = icon), // Use the icon before text
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = Color(0xFF2E3176)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color(0xFF2E3176),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun WindowingUI(modifier: Modifier = Modifier) {
    var sliderPosition by remember { mutableStateOf(30f..70f) }
    var expanded by remember { mutableStateOf(false) }
    var selectedPreset by remember { mutableStateOf("Custom") }
    var selectedIcon by remember {mutableStateOf(R.drawable.ic_list)}

    // Presets dropdown options with corresponding icons
    val presets = listOf(
        "Bone (100, 2400)" to R.drawable.ic_bone, // Assume you have a drawable for each
        "Brain (0, 80)" to R.drawable.ic_brain,
        "Liver (-45, 105)" to R.drawable.ic_liver,
        "Lungs (-1350, 150)" to R.drawable.ic_lung,
        "Muscle (-05, 150)" to R.drawable.ic_muscle
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Threshold Button
                ButtonOption(
                    icon = R.drawable.ic_threshold,
                    text = "Threshold",
                    isSelected = false,
                    onClick = { /* Handle threshold click if needed */ }
                )

                // Presets Dropdown Button
                Box (
                    //modify the spacing and padding here
//                    modifier = Modifier.offset(x = 208.dp)
                ){
                    Button(
                        onClick = { expanded = !expanded },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        shape = MaterialTheme.shapes.medium,
//                        modifier = Modifier.padding(horizontal = 8.dp)
                        modifier = Modifier
                            .width(188.dp) // Set width of the dropdown button
                            .height(32.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start, // Align contents to the start
                            modifier = Modifier.fillMaxWidth() // Fill width for alignment
                        ) {
                            // Icon + Text inside the button
                            Icon(
                                painter = painterResource(id = selectedIcon),
                                contentDescription = "Presets",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(selectedPreset)
                            Spacer(modifier = Modifier.weight(1f)) // Push dropdown arrow to the end


//                        // Left-align the Text and Icon within the button
//                        Box(modifier = Modifier.fillMaxWidth().align(Alignment.Start)) {
//                            Text(selectedPreset, modifier = Modifier.align(Alignment.CenterStart))
//                        }

                            Icon(
                                painter = painterResource(id = R.drawable.ic_dropdown),
                                contentDescription = "Dropdown Arrow",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Dropdown Menu, positioned directly above the button
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(Color.White)
                            .padding(8.dp)
                            .offset(y = (-10).dp) // Slight offset to avoid overlap with threshold
                    ) {
                        presets.forEach { (presetText, icon) ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedPreset = presetText
                                    selectedIcon = icon
                                    expanded = false
                                },
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth().align(Alignment.Start) // Ensure left alignment
                                    ) {
                                        Icon(
                                            painter = painterResource(id = icon),
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = presetText, modifier = Modifier.align(Alignment.CenterVertically))
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }




            Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            RangeSlider(
                value = sliderPosition,
                onValueChange = { range -> sliderPosition = range },
                valueRange = 0f..100f,
                modifier = Modifier.width(360.dp),
                onValueChangeFinished = {
                    // Handle what should happen when the user stops moving the slider
                },
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF50A5DE),
                    activeTrackColor = Color(0xFF50A5DE),
                    inactiveTrackColor = Color(0xFF50A5DE).copy(alpha = 0.5f)
                )
            )
        }

        // Log the current slider values
        println("Min: ${sliderPosition.start}, Max: ${sliderPosition.endInclusive}")

    }
}

@Composable
fun SlicerUI(modifier: Modifier = Modifier) {
    // interactive UI displayed when the "Slicer" button is clicked
    var selectedSetting by remember { mutableStateOf("Transverse") } // Default to Brightness
    var transverseValue by remember { mutableStateOf(30f..70f) } // Initial value for the slider
    var sagittalValue by remember { mutableStateOf(30f..70f) }
    var coronalValue by remember { mutableStateOf(30f..70f) }

    // Main Box for Display UI
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color(0xFFFFFFFF))
            .padding(16.dp)
    ) {
        // Row with three buttons for Brightness, Contrast, and Opacity
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ButtonOption(
                icon = R.drawable.ic_transverse,
                text = "Transverse",
                isSelected = selectedSetting == "Transverse",
                onClick = { selectedSetting = "Transverse" }
            )
            ButtonOption(
                icon = R.drawable.ic_sagittal,
                text = "Sagittal",
                isSelected = selectedSetting == "Sagittal",
                onClick = { selectedSetting = "Sagittal" }
            )
            ButtonOption(
                icon = R.drawable.ic_coronal,
                text = "Coronal",
                isSelected = selectedSetting == "Coronal",
                onClick = { selectedSetting = "Coronal" }
            )
        }

        // Conditionally display the slider based on the selected setting
        when (selectedSetting) {
            "Transverse" -> {
                // Brightness Slider
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    RangeSlider(
                        value = transverseValue,
                        onValueChange = { range -> transverseValue = range },
                        valueRange = 0f..100f,
                        modifier = Modifier.width(360.dp),
                        onValueChangeFinished = {
                            // Handle what should happen when the user stops moving the slider
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF50A5DE),
                            activeTrackColor = Color(0xFF50A5DE),
                            inactiveTrackColor = Color(0xFF50A5DE).copy(alpha = 0.5f)
                        )
                    )
                }
            }
            "Sagittal" -> {
                // Contrast Slider
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    RangeSlider(
                        value = sagittalValue,
                        onValueChange = { range -> sagittalValue = range },
                        valueRange = 0f..100f,
                        modifier = Modifier.width(360.dp),
                        onValueChangeFinished = {
                            // Handle what should happen when the user stops moving the slider
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF50A5DE),
                            activeTrackColor = Color(0xFF50A5DE),
                            inactiveTrackColor = Color(0xFF50A5DE).copy(alpha = 0.5f)
                        )
                    )
                }
            }
            "Coronal" -> {
                // Opacity Slider
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    RangeSlider(
                        value = coronalValue,
                        onValueChange = { range -> coronalValue = range },
                        valueRange = 0f..100f,
                        modifier = Modifier.width(360.dp),
                        onValueChangeFinished = {
                            // Handle what should happen when the user stops moving the slider
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF50A5DE),
                            activeTrackColor = Color(0xFF50A5DE),
                            inactiveTrackColor = Color(0xFF50A5DE).copy(alpha = 0.5f)
                        )
                    )
                }
            }
        }
    }
}


@Composable
fun BottomBarButton(
    icon: Int,
    label: String,
    clickAction: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(horizontal = 22.dp)
            .clickable(onClick = clickAction)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            modifier = Modifier.size(24.dp),
        )
        // More options icon
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ImageDetailViewPreview() {
    Android3DicomTheme {
        ImageDetailView(navController = rememberNavController())
    }
}
