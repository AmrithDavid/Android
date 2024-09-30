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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
                NavHost(navController = navController, startDestination = "scanScreen") {
                    composable("scanScreen") {
                        ScanScreen(navController = navController)
                    }
                    composable("mainImageMenu") {
                        MainImageMenu(navController = navController)
                    }
                    composable("shareView") {
                        ShareView(navController = navController)
                    }
                }
            }
        }
    }
}

    @Composable
    fun ScanScreen(
        viewModel: ScanViewModel = viewModel(),
        navController: NavController,
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
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
        ) { innerPadding ->
            
            if (patientCards.isNotEmpty()) {
                CardList(
                    modifier = Modifier.padding(innerPadding),
                    patientCards = patientCards,
                    onImageButtonClick = { navController.navigate("mainImageMenu") },
                )
            } else {
                EmptyStateView()
            }
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
        ScanScreen(navController = navController)
    }
}
