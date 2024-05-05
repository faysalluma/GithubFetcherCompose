package com.groupec.githubfetchercompose.presentation.navigation

import androidx.navigation.NavController
import com.groupec.githubfetchercompose.R
import com.groupec.githubfetchercompose.presentation.components.detail.DetailScreen
import com.groupec.githubfetchercompose.presentation.components.detailcontributor.DetailContributorScreen
import com.groupec.githubfetchercompose.presentation.components.home.HomeScreen


sealed class NavigationScreen(
    val route: String,
    val titleRes: Int,
) {
    object Home : NavigationScreen("home_screen", R.string.home)
    object Detail : NavigationScreen("detail_screen", R.string.detail)
    object DetailContributor : NavigationScreen("detail_contributor_screen", R.string.detail_contributor)
}

fun NavController.currentDestinationClassName(): String? {
    val navBackStackEntry = currentBackStackEntry
    val destination = navBackStackEntry?.destination
    val route = destination?.route
    return when (route?.substringBeforeLast("/")?.substringBeforeLast("?")) {
        NavigationScreen.Home.route -> HomeScreen::class.java.name
        NavigationScreen.Detail.route -> DetailScreen::class.java.name
        NavigationScreen.DetailContributor.route -> DetailContributorScreen::class.java.name
        else -> null
    }
}