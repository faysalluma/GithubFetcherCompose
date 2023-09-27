package com.groupec.githubfetchercompose.presentation.navigation

import com.groupec.githubfetchercompose.R


sealed class NavigationScreen(
    val route: String,
    val titleRes: Int,
) {
    object Splash : NavigationScreen("splash_screen", R.string.splash)
    object Home : NavigationScreen("home_screen", R.string.home)
    object Operation : NavigationScreen("operation_screen", R.string.operation)
    object Result : NavigationScreen("result_screen", R.string.result)
}