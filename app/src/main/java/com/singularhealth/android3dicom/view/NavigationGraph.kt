@file:Suppress("ktlint:standard:import-ordering", "ktlint:standard:no-wildcard-imports")

package com.singularhealth.android3dicom.view

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.singularhealth.android3dicom.model.AppState
import com.singularhealth.android3dicom.model.LoginPreferenceOption
import com.singularhealth.android3dicom.view.components.*
import com.singularhealth.android3dicom.viewmodel.LoginViewModel
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
    BIOMETRIC_LOGIN,
    PIN_SETUP,
    BIOMETRIC_SETUP_PLACEHOLDER,
    SCAN_LIBRARY,
    IMAGE_DETAIL,
    SHARE,
    REPORT,
    PIN_VERIFICATION,
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val searchQuery = remember { mutableStateOf("") }

    val appState: AppState =
        EntryPoints
            .get(
                LocalContext.current as ComponentActivity,
                IAppStateEntryPoint::class.java,
            ).appState()

    appState.setNavController(navController)

    var startRoute = getStartRoute(appState)
    if (startRoute != ViewRoute.LOGIN.toString()) {
        appState.renewStoredLogin()
    }

    NavHost(
        navController = navController,
        startDestination = startRoute,
    ) {
        composable(ViewRoute.LOGIN.toString()) {
            LoginScreen()
        }
        composable(ViewRoute.LOGIN_SETUP.toString()) {
            LoginSetupView(
                onBackClick = {
                    navController.navigate(ViewRoute.LOGIN.toString()) {
                        popUpTo(ViewRoute.LOGIN_SETUP.toString()) { inclusive = true }
                    }
                },
                onNavigateToPinSetup = {
                    navController.navigate(ViewRoute.PIN_SETUP.toString())
                },
                onNavigateToBiometricSetup = {
                    navController.navigate(ViewRoute.BIOMETRIC_SETUP_PLACEHOLDER.toString())
                },
                onNavigateToLogin = {
                    navController.navigate(ViewRoute.LOGIN.toString()) {
                        popUpTo(ViewRoute.LOGIN_SETUP.toString()) { inclusive = true }
                    }
                },
            )
        }
        composable(ViewRoute.BIOMETRIC_LOGIN.toString()) {
            BiometricLoginView()
        }
        composable(ViewRoute.PIN_SETUP.toString()) {
            PinSetupScreen(
                onSetupSuccess = {
                    navController.navigate(ViewRoute.PIN_VERIFICATION.toString())
                },
                onCancel = {
                    navController.navigate(ViewRoute.LOGIN.toString()) {
                        popUpTo(ViewRoute.LOGIN.toString()) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
        composable(ViewRoute.BIOMETRIC_SETUP_PLACEHOLDER.toString()) {
            BiometricSetupPlaceholderScreen(
                onSetupComplete = {
                    navController.navigate(ViewRoute.SCAN_LIBRARY.toString()) {
                        popUpTo(ViewRoute.LOGIN_SETUP.toString()) { inclusive = true }
                    }
                },
            )
        }
        composable(ViewRoute.PIN_VERIFICATION.toString()) {
            PinVerificationScreen(
                onVerificationSuccess = {
                    navController.navigate(ViewRoute.SCAN_LIBRARY.toString()) {
                        popUpTo(ViewRoute.PIN_VERIFICATION.toString()) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(ViewRoute.LOGIN.toString()) {
                        popUpTo(ViewRoute.PIN_VERIFICATION.toString()) { inclusive = true }
                    }
                },
                onForgotPin = {
                    navController.navigate(ViewRoute.LOGIN.toString()) {
                        popUpTo(ViewRoute.PIN_VERIFICATION.toString()) { inclusive = true }
                    }
                },
            )
        }
        composable(ViewRoute.SCAN_LIBRARY.toString()) {
            ScanLibraryView(
                searchQuery = searchQuery,
                onLogout = {
                    loginViewModel.logoutUser()
                    navController.navigate(ViewRoute.LOGIN.toString()) {
                        popUpTo(ViewRoute.SCAN_LIBRARY.toString()) { inclusive = true }
                    }
                },
            )
        }
        composable(ViewRoute.IMAGE_DETAIL.toString()) {
            ImageDetailView()
        }
        composable(ViewRoute.REPORT.toString()) {
            ReportView()
        }
        composable(ViewRoute.SHARE.toString()) {
            ShareView()
        }
    }

    SideEffect {
        searchQuery.value = searchQuery.value
    }
}

@Composable
fun getStartRoute(appState: AppState): String =
    when (appState.loginPreference) {
        LoginPreferenceOption.NONE -> ViewRoute.LOGIN.toString()
        LoginPreferenceOption.BIOMETRIC -> ViewRoute.BIOMETRIC_LOGIN.toString()
        LoginPreferenceOption.PIN -> {
            if (appState.isPinSet()) {
                ViewRoute.PIN_VERIFICATION.toString()
            } else {
                ViewRoute.LOGIN.toString()
            }
        }
    }
