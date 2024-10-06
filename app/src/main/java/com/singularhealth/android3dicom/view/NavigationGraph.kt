package com.singularhealth.android3dicom.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.singularhealth.android3dicom.utilities.BiometricUtils
import com.singularhealth.android3dicom.view.components.LoginScreen
import com.singularhealth.android3dicom.view.components.ShareView
import com.singularhealth.android3dicom.viewmodel.LoginViewModel
import com.singularhealth.android3dicom.viewmodel.LoginViewModelFactory

@Suppress("ktlint:standard:function-naming")
@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(LocalContext.current))
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsStateWithLifecycle()
    val searchQuery = remember { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = "loginSetup", // if (isLoggedIn) "scanScreen" else "login"
    ) {
        composable("loginSetup") {
            LoginSetupView(
                onBackClick = { /* TODO: Handle back click if needed */ },
                onBiometricLoginClick = {
                    BiometricUtils.authenticate()
                },
                navController,
            )
        }
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("scanScreen") {
                        popUpTo("login") { inclusive = true }
                    }
                },
            )
        }
        composable("scanScreen") {
            ScanScreen(
                navController = navController,
                searchQuery = searchQuery,
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
