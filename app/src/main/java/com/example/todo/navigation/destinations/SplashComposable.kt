package com.example.todo.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.todo.ui.screens.splash.SplashScreen
import com.example.todo.util.Constants.SPLASH_SCREEN

fun NavGraphBuilder.splashComposable(
    navigateToListScreen: () -> Unit,
) {
    composable(
        route = SPLASH_SCREEN,
        ) {
        SplashScreen(navigateToListScreen=navigateToListScreen)

    }
}