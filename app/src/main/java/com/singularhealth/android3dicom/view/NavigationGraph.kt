package com.singularhealth.android3dicom.view

import androidx.activity.ComponentActivity
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
import com.singularhealth.android3dicom.model.AppState
import com.singularhealth.android3dicom.model.LoginPreferenceOption
import com.singularhealth.android3dicom.utilities.BiometricUtils
import com.singularhealth.android3dicom.view.components.ShareView
import com.singularhealth.android3dicom.viewmodel.LoginViewModel
import com.singularhealth.android3dicom.viewmodel.LoginViewModelFactory
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface IAppStateEntryPoint {
    fun appState(): AppState
}

enum class ViewRoute {
    LOGIN,
    LOGIN_SETUP,
    SCAN_LIBRARY,
    IMAGE_DETAIL,
    SHARE,
    REPORT,
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(LocalContext.current))
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsStateWithLifecycle()
    val searchQuery = remember { mutableStateOf("") }

    // Inject the AppState via the entrypoint defined above
    val appState: AppState =
        EntryPoints
            .get(
                LocalContext.current as ComponentActivity,
                IAppStateEntryPoint::class.java,
            ).appState()

    NavHost(
        navController = navController,
        startDestination = getStartRoute(),
    ) {
        composable(ViewRoute.LOGIN.toString()) {
            LoginScreen(
                onLoginSuccess = {
                    if (appState.loginPreference == LoginPreferenceOption.NONE) {
                        navController.navigate(ViewRoute.LOGIN_SETUP.toString()) {
                            popUpTo(ViewRoute.LOGIN.toString()) { inclusive = true }
                        }
                    } else {
                        navController.navigate(ViewRoute.SCAN_LIBRARY.toString()) {
                            popUpTo(ViewRoute.LOGIN.toString()) { inclusive = true }
                        }
                    }
                },
            )
        }
        composable(ViewRoute.LOGIN_SETUP.toString()) {
            LoginSetupView(
                onBackClick = { /* TODO: Handle back click if needed */ },
                onBiometricLoginClick = {
                    BiometricUtils.authenticate()
                },
                onSetupSuccess = {
                    when (appState.loginPreference) {
                        LoginPreferenceOption.NONE -> {
                            navController.navigate(ViewRoute.SCAN_LIBRARY.toString()) {
                                popUpTo(ViewRoute.LOGIN.toString()) { inclusive = true }
                            }
                        }
                        LoginPreferenceOption.BIOMETRIC -> {
                            // replace with biometric login
                            navController.navigate(ViewRoute.SCAN_LIBRARY.toString()) {
                                popUpTo(ViewRoute.LOGIN_SETUP.toString()) { inclusive = true }
                            }
                        }
                        LoginPreferenceOption.PIN -> {
                            // replace with PIN login
                            navController.navigate(ViewRoute.SHARE.toString()) {
                                popUpTo(ViewRoute.LOGIN.toString()) { inclusive = true }
                            }
                        }
                    }
                },
            )
        }
        composable(ViewRoute.SCAN_LIBRARY.toString()) {
            ScanLibraryView(
                navController = navController,
                searchQuery = searchQuery,
                onLogout = {
                    loginViewModel.logoutUser()
                    navController.navigate("login") {
                        popUpTo("scanScreen") { inclusive = true }
                    }
                },
            )
        }
        composable(ViewRoute.IMAGE_DETAIL.toString()) {
            ImageDetailView(navController = navController)
        }
        composable(ViewRoute.SHARE.toString()) {
            ShareView(navController = navController)
        }
    }

    // Pass a lambda to update the search query
    SideEffect {
        searchQuery.value = searchQuery.value
    }
}

@Composable
fun getStartRoute(): String {
    val appState: AppState =
        EntryPoints
            .get(
                LocalContext.current as ComponentActivity,
                IAppStateEntryPoint::class.java,
            ).appState()

    return when (appState.loginPreference) {
        LoginPreferenceOption.NONE -> ViewRoute.LOGIN.toString()
        LoginPreferenceOption.BIOMETRIC -> ViewRoute.SCAN_LIBRARY.toString() // replace with biometric login
        LoginPreferenceOption.PIN -> ViewRoute.REPORT.toString() // replace with PIN login
    }
}
