package com.groupec.githubfetchercompose.presentation.navigation

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.facebook.flipper.plugins.navigation.NavigationFlipperPlugin
import com.google.gson.Gson
import com.groupec.githubfetchercompose.data.dto.ContributorDTO
import com.groupec.githubfetchercompose.data.dto.RepositoryDTO
import com.groupec.githubfetchercompose.presentation.components.detail.DetailScreen
import com.groupec.githubfetchercompose.presentation.components.detailcontributor.DetailContributorScreen
import com.groupec.githubfetchercompose.presentation.components.home.HomeScreen


class FlipperNavigationLogger(private val flipperPlugin: NavigationFlipperPlugin) : NavController.OnDestinationChangedListener {
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        val route = destination.route
        // Log the navigation event to Flipper
        flipperPlugin.sendNavigationEvent(route, controller.currentDestinationClassName(), null)
    }
}
@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier,
    activity: Activity,
) {

    val flipperPlugin = NavigationFlipperPlugin.getInstance()
    val flipperLogger = FlipperNavigationLogger(flipperPlugin)
    navController.addOnDestinationChangedListener(flipperLogger)

    NavHost(navController, startDestination = NavigationScreen.Home.route, modifier) {
        composable(NavigationScreen.Home.route) {
            HomeScreen().Screen(
                params = HomeScreen.Params { repository ->
                    val jsonTransaction = Uri.encode(Gson().toJson(repository))
                    navController.navigate(NavigationScreen.Detail.route.plus("/${jsonTransaction}"))
                },
                navigationScreen = NavigationScreen.Home,
                activity = activity
            )
        }
        composable(NavigationScreen.Detail.route.plus("/{repository}"),
            arguments = mutableListOf(
                navArgument("repository") {
                    type = RepositoryDTO.NavigationType
                    nullable = false
                }
            )
        ) { backStack ->
            val repository = backStack.arguments?.getParcelable<RepositoryDTO>("repository")
            DetailScreen().Screen(
                params = repository?.let { DetailScreen.Params(it){ contributor ->
                        val jsonTransaction = Uri.encode(Gson().toJson(contributor))
                        navController.navigate(NavigationScreen.DetailContributor.route.plus("/${jsonTransaction}"))
                    }
                },
                navigationScreen = NavigationScreen.Detail,
                activity = activity
            )
        }
        composable(NavigationScreen.DetailContributor.route.plus("/{contributor}"),
            arguments = mutableListOf(
                navArgument("contributor") {
                    type = ContributorDTO.NavigationType
                    nullable = false
                }
            )
        ) { backStack ->
            val contributor = backStack.arguments?.getParcelable<ContributorDTO>("contributor")
            DetailContributorScreen().Screen(
                params = contributor?.let {
                    DetailContributorScreen.Params(it){
                        navController.popBackStack()
                    }
                },
                navigationScreen = NavigationScreen.DetailContributor,
                activity = activity
            )
        }
    }
}