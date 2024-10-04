// Package declaration for the app
@file:Suppress("ktlint:standard:no-wildcard-imports", "ktlint:standard:function-naming")

package com.singularhealth.android3dicom.view

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.singularhealth.android3dicom.model.PatientCardData
import com.singularhealth.android3dicom.ui.theme.Android3DicomTheme
import com.singularhealth.android3dicom.ui.theme.DarkBlue
import com.singularhealth.android3dicom.view.components.EmptyStateView
import com.singularhealth.android3dicom.view.components.NavigationBar
import com.singularhealth.android3dicom.view.components.ScanCard
import com.singularhealth.android3dicom.view.components.ScanLibraryMenu
import com.singularhealth.android3dicom.view.components.ShareView
import com.singularhealth.android3dicom.viewmodel.ScanLibraryViewModel

class ScanLibraryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            Android3DicomTheme {
                val navController = rememberNavController()
                val searchQuery = remember { mutableStateOf("") }

                NavHost(navController = navController, startDestination = "scanScreen") {
                    composable("scanScreen") {
                        ScanScreen(
                            navController = navController,
                            searchQuery = searchQuery
                        )
                    }
                    composable("mainImageMenu") {
                        MainImageMenu(navController = navController)
                    }
                    composable("shareView") {
                        ShareView(navController = navController)
                    }
                }

                // Pass a lambda to update the search query
                SideEffect {
                    searchQuery.value = searchQuery.value
                }

            }
        }
    }
}

    @Composable
    fun ScanScreen(
        viewModel: ScanLibraryViewModel = viewModel(),
        navController: NavController,
        searchQuery: MutableState<String>,
    ) {
        val greeting by viewModel.greeting.collectAsState()
        val patientCards by viewModel.patientCards.collectAsState()
        val isSideMenuVisible by viewModel.isSideMenuVisible.collectAsState()

        // Control system UI color
        val view = LocalView.current
        if (!view.isInEditMode) {
            SideEffect {
                val window = (view.context as Activity).window
                window.statusBarColor =
                    if (isSideMenuVisible) {
                        Color.Transparent.toArgb()
                    } else {
                        DarkBlue.toArgb()
                    }
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isSideMenuVisible
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                NavigationBar(
                    greeting = greeting,
                    onMenuClick = { viewModel.toggleSideMenu() },
                    searchQuery = searchQuery,
                    onSearchQueryChange = { newQuery ->
                        // updates state
                        searchQuery.value = newQuery.value // Update the search query state
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
        ) { innerPadding ->
            val filteredCards = patientCards.filter { card ->
                card.patientName.contains(searchQuery.value, ignoreCase = true) ||
                card.patientId.contains(searchQuery.value, ignoreCase = true) ||
                card.modality.contains(searchQuery.value, ignoreCase = true) ||
                card.fileName.contains(searchQuery.value, ignoreCase = true)
            }
            
            if (filteredCards.isNotEmpty()) {
                CardList(
                    modifier = Modifier.padding(innerPadding),
                    patientCards = filteredCards,
                    onImageButtonClick = { navController.navigate("mainImageMenu") },
                )
            } else {
                EmptyStateView()
            }
        }
        // Semi-transparent overlay
        if (isSideMenuVisible) {
            Box(
                modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color(0x52000000)), // #00000052
            )
        }

        // SideMenu
        if (isSideMenuVisible) {
            ScanLibraryMenu(
                onCloseMenu = { viewModel.toggleSideMenu() },
                onHomeClick = { viewModel.onHomeClick() },
                onClearCacheClick = { viewModel.onClearCacheClick() },
                onBiometricClick = { viewModel.onBiometricClick() },
                onAboutClick = { viewModel.onAboutClick() },
                onSupportClick = { viewModel.onSupportClick() },
                onLogoutClick = { viewModel.onLogoutClick() },
            )
        }
    }


// CardList displays a list of ScanCard items
@Composable
fun CardList(
    modifier: Modifier = Modifier,
    patientCards: List<PatientCardData>,
    onImageButtonClick: () -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(patientCards.size) { index ->
            ScanCard(
                patientCardData = patientCards[index],
                onImageButtonClick = onImageButtonClick,
            )
        }
    }
}

// Visualise the ScanScreen
@Preview(showBackground = true)
@Composable
fun ScanScreenPreview() {
    Android3DicomTheme {
        val navController = rememberNavController()
        val searchQuery = remember { mutableStateOf("") } // Create MutableState<String>

        ScanScreen(
            navController = navController,
            searchQuery = searchQuery
        )
    }
}
