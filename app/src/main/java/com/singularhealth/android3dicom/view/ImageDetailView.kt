package com.singularhealth.android3dicom.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.singularhealth.android3dicom.R
import com.singularhealth.android3dicom.ui.theme.Android3DicomTheme
import com.singularhealth.android3dicom.ui.theme.LightBlue
import com.singularhealth.android3dicom.ui.theme.SubheadingColor
import com.singularhealth.android3dicom.view.components.ImageDetailOptionsMenu
import com.singularhealth.android3dicom.view.components.LoadingSpinner
import com.singularhealth.android3dicom.view.components.SupportDialog
import com.singularhealth.android3dicom.viewmodel.ImageDetailViewModel

@Composable
fun ImageDetailView(viewModel: ImageDetailViewModel = hiltViewModel()) {
    // State variables
    var showDropdown by remember { mutableStateOf(false) }
    var showSupportDialog by remember { mutableStateOf(false) }
    var showMoreInfoDialog by remember { mutableStateOf(false) }
    var selectedButton by remember { mutableStateOf("3D") }
    var currentView by remember { mutableStateOf("None") }
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()

    // Shared slider state variables
    var displayBrightness by remember { mutableStateOf(0.5f) }
    var displayContrast by remember { mutableStateOf(0.5f) }
    var displayOpacity by remember { mutableStateOf(0.5f) }
    var windowingRange by remember { mutableStateOf(30f..70f) }
    var slicerTransverse by remember { mutableStateOf(30f..70f) }
    var slicerSagittal by remember { mutableStateOf(30f..70f) }
    var slicerCoronal by remember { mutableStateOf(30f..70f) }

    val isInitialLoading by viewModel.isInitialLoading.collectAsState()

    var selectedPreset by remember { mutableStateOf(ImageDetailViewModel.WindowingPreset.CUSTOM) }

    Android3DicomTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            ImageDetailTopBar(
                selectedButton = selectedButton,
                onButtonSelected = { selectedButton = it },
                on3DClick = { viewModel.on3DClick() },
                onTransverseClick = { viewModel.onTransverseClick() },
                onSagittalClick = { viewModel.onSagittalClick() },
                onCoronalClick = { viewModel.onCoronalClick() },
                onMoreClick = { showDropdown = true },
                onBackClick = { viewModel.onBack() },
            )

            // Content area
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 0.dp),
            ) {
                if (isInitialLoading) {
                    LoadingSpinner(modifier = Modifier.align(Alignment.Center))
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

                    when (currentView) {
                        "Display" ->
                            DisplayUI(
                                brightnessValue = displayBrightness,
                                onBrightnessChange = { value ->
                                    displayBrightness = value
                                    viewModel.onBrightnessSliderUpdate(value) // Update ViewModel
                                },
                                contrastValue = displayContrast,
                                onContrastChange = { value ->
                                    displayContrast = value
                                    viewModel.onContrastSliderUpdate(value) // Update ViewModel
                                },
                                opacityValue = displayOpacity,
                                onOpacityChange = { value ->
                                    displayOpacity = value
                                    viewModel.onOpacitySliderUpdate(value) // Update ViewModel
                                },
                                modifier =
                                    Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth()
                                        .offset(y = 16.dp),
                            )
                        "Windowing" ->
                            WindowingUI(
                                sliderRange = windowingRange,
                                onPresetChange = { preset ->
                                    selectedPreset = preset // Update the selected preset state
                                },
                                onRangeChange = { range ->
                                    windowingRange = range
                                    viewModel.onWindowingSliderUpdate(
                                        preset = selectedPreset,
                                        upper_limit = range.endInclusive,
                                        lower_limit = range.start,
                                    ) // Update ViewModel
                                },
                                modifier =
                                    Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth()
                                        .offset(y = 16.dp),
                            )
                        "Slicer" ->
                            SlicerUI(
                                transverseValue = slicerTransverse,
                                onTransverseChange = { range ->
                                    slicerTransverse = range
                                    viewModel.onSlicerSliderUpdate(
                                        option = ImageDetailViewModel.SlicerView.TRANSVERSE,
                                        upper_limit = range.endInclusive,
                                        lower_limit = range.start,
                                    ) // Update ViewModel
                                },
                                sagittalValue = slicerSagittal,
                                onSagittalChange = { range ->
                                    slicerSagittal = range
                                    viewModel.onSlicerSliderUpdate(
                                        option = ImageDetailViewModel.SlicerView.SAGITTAL,
                                        upper_limit = range.endInclusive,
                                        lower_limit = range.start,
                                    ) // Update ViewModel
                                },
                                coronalValue = slicerCoronal,
                                onCoronalChange = { range ->
                                    slicerCoronal = range
                                    viewModel.onSlicerSliderUpdate(
                                        option = ImageDetailViewModel.SlicerView.CORONAL,
                                        upper_limit = range.endInclusive,
                                        lower_limit = range.start,
                                    ) // Update ViewModel
                                },
                                modifier =
                                    Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth()
                                        .offset(y = 16.dp),
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
                                    "Share" -> viewModel.onShare()
                                    "More Info" -> {
                                        showMoreInfoDialog = true
                                        showDropdown = false
                                    }
                                    "Delete" -> viewModel.onDelete()
                                    "Report" -> viewModel.onReport()
                                    "Support" -> {
                                        showSupportDialog = true
                                        showDropdown = false
                                    }
                                }
                            },
                        )
                    }
                }
            }

            ImageDetailBottomBar(
                onDisplayClick = {
                    currentView = if (currentView == "Display") "None" else "Display"
                },
                onWindowingClick = {
                    currentView = if (currentView == "Windowing") "None" else "Windowing"
                },
                onSlicerClick = {
                    currentView = if (currentView == "Slicer") "None" else "Slicer"
                },
            )

            // Show the delete confirmation dialog
            if (showDeleteDialog) {
                println("View: showing dialog")
                DeleteConfirmationDialog(
                    onDismiss = { viewModel.onDeleteDialogDismiss() },
                    onConfirmDelete = { viewModel.onDeleteDialogConfirm() },
                )
            }
        }
    }

    // Support Dialog
    if (showSupportDialog) {
        SupportDialog(
            onDismissRequest = { showSupportDialog = false },
            context = LocalContext.current,
        )
    }

    // More Info Dialog
    if (showMoreInfoDialog) {
        Dialog(onDismissRequest = { showMoreInfoDialog = false }) {
            Box(
                modifier =
                    Modifier
                        .size(width = 280.dp, height = 273.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(24.dp),
                        ),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_info),
                        contentDescription = "Information",
                        tint = Color.Black,
                        modifier = Modifier.size(32.dp),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Study description",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(modifier = Modifier.fillMaxWidth()) {
                        InfoRow("Date added:", "dd/mm/yyyy")
                        InfoRow("Image count:", "257")
                        InfoRow("Series description:", "Chest")
                        InfoRow("Series number:", "3")
                        InfoRow("Instance ID:", "1.2.3.4.5.6.789")
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Box(modifier = Modifier.fillMaxWidth()) {
                        TextButton(
                            onClick = { showMoreInfoDialog = false },
                            modifier = Modifier.align(Alignment.BottomEnd),
                            colors =
                                ButtonDefaults.textButtonColors(
                                    contentColor = LightBlue,
                                ),
                        ) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier =
                Modifier
                    .size(width = 280.dp, height = 240.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Delete",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF1D1D1F),
                )
                Text(
                    text = "Delete scan?",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Deleting this scan will mean it is no longer accessible on this device or through the web portal.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = SubheadingColor),
                    textAlign = TextAlign.Left,
                    lineHeight = 15.sp,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color(0xFF606066))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(onClick = {
                        onConfirmDelete()
                    }) {
                        Text("Delete", color = Color(0xFF50A5DE))
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 1.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = SubheadingColor,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = SubheadingColor,
        )
    }
}

@Composable
fun ImageDetailTopBar(
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
fun ImageDetailBottomBar(
    onDisplayClick: () -> Unit,
    onWindowingClick: () -> Unit,
    onSlicerClick: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.fillMaxWidth(),
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
                },
            )
            BottomBarButton(
                icon = R.drawable.ic_windowing,
                label = "Windowing",
                clickAction = {
                    // Action to perform when button is clicked
                    println("Windowing button clicked!")
                    onWindowingClick()
                },
            )
            BottomBarButton(
                icon = R.drawable.ic_slicer,
                label = "Slicer",
                clickAction = {
                    // Action to perform when button is clicked
                    println("Slicer button clicked!")
                    onSlicerClick()
                },
            )
        }
    }
}

@Composable
fun DisplayUI(
    brightnessValue: Float, // Shared value for brightness
    onBrightnessChange: (Float) -> Unit, // Callback to update brightness value
    contrastValue: Float, // Shared value for contrast
    onContrastChange: (Float) -> Unit, // Callback to update contrast value
    opacityValue: Float, // Shared value for opacity
    onOpacityChange: (Float) -> Unit, // Callback to update opacity value
    modifier: Modifier = Modifier,
) {
    // interactive UI displayed when the "Display" button is clicked
    var selectedSetting by remember { mutableStateOf("Brightness") } // Default to Brightness

    // Main Box for Display UI
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(color = Color(0xFFFFFFFF))
                .padding(16.dp),
    ) {
        // Row with three buttons for Brightness, Contrast, and Opacity
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            ButtonOption(
                icon = R.drawable.ic_brightness,
                text = "Brightness",
                isSelected = selectedSetting == "Brightness",
                onClick = { selectedSetting = "Brightness" },
            )
            ButtonOption(
                icon = R.drawable.ic_contrast,
                text = "Contrast",
                isSelected = selectedSetting == "Contrast",
                onClick = { selectedSetting = "Contrast" },
            )
            ButtonOption(
                icon = R.drawable.ic_opacity,
                text = "Opacity",
                isSelected = selectedSetting == "Opacity",
                onClick = { selectedSetting = "Opacity" },
            )
        }

        // Conditionally display the slider based on the selected setting
        when (selectedSetting) {
            "Brightness" -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Slider(
                        value = brightnessValue, // Use the shared state for brightness
                        onValueChange = onBrightnessChange, // Update brightness state
                        modifier = Modifier.width(360.dp),
                        colors =
                            SliderDefaults.colors(
                                thumbColor = Color(0xFF50A5DE),
                                activeTrackColor = Color(0xFF50A5DE),
                                inactiveTrackColor = Color(0xFF50A5DE).copy(alpha = 0.5f),
                            ),
                    )
                }
            }
            "Contrast" -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Slider(
                        value = contrastValue, // Use the shared state for contrast
                        onValueChange = onContrastChange, // Update contrast state
                        modifier = Modifier.width(360.dp),
                        colors =
                            SliderDefaults.colors(
                                thumbColor = Color(0xFF50A5DE),
                                activeTrackColor = Color(0xFF50A5DE),
                                inactiveTrackColor = Color(0xFF50A5DE).copy(alpha = 0.5f),
                            ),
                    )
                }
            }
            "Opacity" -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Slider(
                        value = opacityValue, // Use the shared state for opacity
                        onValueChange = onOpacityChange, // Update opacity state
                        modifier = Modifier.width(360.dp),
                        colors =
                            SliderDefaults.colors(
                                thumbColor = Color(0xFF50A5DE),
                                activeTrackColor = Color(0xFF50A5DE),
                                inactiveTrackColor = Color(0xFF50A5DE).copy(alpha = 0.5f),
                            ),
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
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = if (isSelected) Color(0xFFE0E7FF) else Color.Transparent,
                contentColor = Color(0xFFF0F1FF),
            ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp), // Reduced padding inside the button
        modifier = Modifier.padding(horizontal = 4.dp), // Reduced padding between the buttons
    ) {
        Icon(
            painter = painterResource(id = icon), // Use the icon before text
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = Color(0xFF2E3176),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color(0xFF2E3176),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
fun WindowingUI(
    sliderRange: ClosedFloatingPointRange<Float>, // Shared range for Windowing slider
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit, // Callback to update the range
    modifier: Modifier = Modifier,
    onPresetChange: (ImageDetailViewModel.WindowingPreset) -> Unit, // New callback for preset changes
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedPreset by remember { mutableStateOf(ImageDetailViewModel.WindowingPreset.CUSTOM) }

    // dictionary of preset icon, text and enum
    val presetMap =
        mapOf(
            ImageDetailViewModel.WindowingPreset.BONES to ("Bone (100, 2400)" to R.drawable.ic_bone),
            ImageDetailViewModel.WindowingPreset.BRAIN to ("Brain (0, 80)" to R.drawable.ic_brain),
            ImageDetailViewModel.WindowingPreset.LIVER to ("Liver (-45, 105)" to R.drawable.ic_liver),
            ImageDetailViewModel.WindowingPreset.LUNGS to ("Lungs (-1350, 150)" to R.drawable.ic_lung),
            ImageDetailViewModel.WindowingPreset.MUSCLE to ("Muscle (-05, 150)" to R.drawable.ic_muscle),
        )

    val (selectedText, selectedIcon) = presetMap[selectedPreset] ?: ("Custom" to R.drawable.ic_list)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(16.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Threshold Button
                ButtonOption(
                    icon = R.drawable.ic_threshold,
                    text = "Threshold",
                    isSelected = false,
                    onClick = { /* Handle threshold click if needed */ },
                )

                // Presets Dropdown Button
                Box(
                    // modify the spacing and padding here
                    // modifier = Modifier.offset(x = 208.dp)
                ) {
                    Button(
                        onClick = { expanded = !expanded },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black,
                            ),
                        shape = MaterialTheme.shapes.medium,
                        modifier =
                            Modifier
                                .width(188.dp) // Set width of the dropdown button
                                .height(32.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start, // Align contents to the start
                            modifier = Modifier.fillMaxWidth(), // Fill width for alignment
                        ) {
                            // Icon + Text inside the button
                            Icon(
                                painter = painterResource(id = selectedIcon),
                                contentDescription = "Preset Icon",
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(selectedText)
                            Spacer(modifier = Modifier.weight(1f)) // Push dropdown arrow to the end

                            Icon(
                                painter = painterResource(id = R.drawable.ic_dropdown),
                                contentDescription = "Dropdown Arrow",
                                modifier = Modifier.size(18.dp),
                            )
                        }
                    }

                    // Dropdown Menu, positioned directly above the button
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier =
                            Modifier
                                .background(Color.White)
                                .padding(8.dp)
                                .offset(y = (-10).dp), // Slight offset to avoid overlap with threshold
                    ) {
                        presetMap.forEach { (preset, value) ->
                            val (text, icon) = value
                            DropdownMenuItem(
                                onClick = {
                                    selectedPreset = preset
                                    onPresetChange(preset) // Notify parent about the change
                                    expanded = false
                                },
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth().align(Alignment.Start), // Ensure left alignment
                                    ) {
                                        Icon(
                                            painter = painterResource(id = icon),
                                            contentDescription = "Preset Icon",
                                            modifier = Modifier.size(20.dp),
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = text)
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            RangeSlider(
                value = sliderRange, // Use the shared state for the range slider
                onValueChange = onRangeChange, // Update the range
                valueRange = 0f..100f,
                modifier = Modifier.width(360.dp),
//                onValueChangeFinished = {
//                    // Handle what should happen when the user stops moving the slider
//                },
                colors =
                    SliderDefaults.colors(
                        thumbColor = Color(0xFF50A5DE),
                        activeTrackColor = Color(0xFF50A5DE),
                        inactiveTrackColor = Color(0xFF50A5DE).copy(alpha = 0.5f),
                    ),
            )
        }

        // Log the current slider values
        println("Min: ${sliderRange.start}, Max: ${sliderRange.endInclusive}")
    }
}

@Composable
fun SlicerUI(
    transverseValue: ClosedFloatingPointRange<Float>, // Shared range for Transverse
    onTransverseChange: (ClosedFloatingPointRange<Float>) -> Unit, // Callback to update Transverse range
    sagittalValue: ClosedFloatingPointRange<Float>, // Shared range for Sagittal
    onSagittalChange: (ClosedFloatingPointRange<Float>) -> Unit, // Callback to update Sagittal range
    coronalValue: ClosedFloatingPointRange<Float>, // Shared range for Coronal
    onCoronalChange: (ClosedFloatingPointRange<Float>) -> Unit, // Callback to update Coronal range
    modifier: Modifier = Modifier,
) {
    // interactive UI displayed when the "Slicer" button is clicked
    var selectedSetting by remember { mutableStateOf("Transverse") } // Default to Brightness

    // Main Box for Display UI
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(color = Color(0xFFFFFFFF))
                .padding(16.dp),
    ) {
        // Row with three buttons for Brightness, Contrast, and Opacity
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            ButtonOption(
                icon = R.drawable.ic_transverse,
                text = "Transverse",
                isSelected = selectedSetting == "Transverse",
                onClick = { selectedSetting = "Transverse" },
            )
            ButtonOption(
                icon = R.drawable.ic_sagittal,
                text = "Sagittal",
                isSelected = selectedSetting == "Sagittal",
                onClick = { selectedSetting = "Sagittal" },
            )
            ButtonOption(
                icon = R.drawable.ic_coronal,
                text = "Coronal",
                isSelected = selectedSetting == "Coronal",
                onClick = { selectedSetting = "Coronal" },
            )
        }

        // Conditionally display the slider based on the selected setting
        when (selectedSetting) {
            "Transverse" -> {
                // Transverse Slider
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    RangeSlider(
                        value = transverseValue,
                        onValueChange = onTransverseChange,
                        valueRange = 0f..100f,
                        modifier = Modifier.width(360.dp),
                        colors =
                            SliderDefaults.colors(
                                thumbColor = Color(0xFF50A5DE),
                                activeTrackColor = Color(0xFF50A5DE),
                                inactiveTrackColor = Color(0xFF50A5DE).copy(alpha = 0.5f),
                            ),
                    )
                }
            }
            "Sagittal" -> {
                // Contrast Slider
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    RangeSlider(
                        value = sagittalValue,
                        onValueChange = onSagittalChange,
                        valueRange = 0f..100f,
                        modifier = Modifier.width(360.dp),
                        onValueChangeFinished = {
                            // Handle what should happen when the user stops moving the slider
                        },
                        colors =
                            SliderDefaults.colors(
                                thumbColor = Color(0xFF50A5DE),
                                activeTrackColor = Color(0xFF50A5DE),
                                inactiveTrackColor = Color(0xFF50A5DE).copy(alpha = 0.5f),
                            ),
                    )
                }
            }
            "Coronal" -> {
                // Opacity Slider
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    RangeSlider(
                        value = coronalValue,
                        onValueChange = onCoronalChange,
                        valueRange = 0f..100f,
                        modifier = Modifier.width(360.dp),
                        onValueChangeFinished = {
                            // Handle what should happen when the user stops moving the slider
                        },
                        colors =
                            SliderDefaults.colors(
                                thumbColor = Color(0xFF50A5DE),
                                activeTrackColor = Color(0xFF50A5DE),
                                inactiveTrackColor = Color(0xFF50A5DE).copy(alpha = 0.5f),
                            ),
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
        modifier =
            Modifier
                .padding(horizontal = 22.dp)
                .clickable(onClick = clickAction),
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
        ImageDetailView()
    }
}
