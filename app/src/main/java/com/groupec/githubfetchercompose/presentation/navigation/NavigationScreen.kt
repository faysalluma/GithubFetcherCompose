package com.groupec.githubfetchercompose.presentation.navigation

import com.groupec.githubfetchercompose.R


sealed class NavigationScreen(
    val route: String,
    val titleRes: Int,
) {
    object Home : NavigationScreen("home_screen", R.string.home)
    object Detail : NavigationScreen("detail_screen", R.string.detail)
    object DetailContributor : NavigationScreen("detail_contributor_screen", R.string.detail_contributor)
}