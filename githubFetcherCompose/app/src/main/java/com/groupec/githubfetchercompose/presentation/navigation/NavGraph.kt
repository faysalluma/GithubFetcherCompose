package com.groupec.githubfetchercompose.presentation.navigation

import android.app.Activity
import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.groupec.githubfetchercompose.presentation.components.operation.OperationScreen
import com.groupec.githubfetchercompose.presentation.components.result.ResultScreen
import com.groupec.famocopayapp2apptoolscompose.presentation.components.home.HomeScreen
import com.groupec.githubfetchercompose.presentation.components.splash.SplashScreen
import com.google.gson.Gson

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier,
    activity: Activity,
) {
    /*var myResult by remember { mutableStateOf<ResultDTO?>(null) }
    var myTransaction by remember { mutableStateOf<TransactionDTO?>(null) }*/

    NavHost(navController, startDestination = NavigationScreen.Splash.route, modifier) {
        composable(NavigationScreen.Splash.route) {
            val onNextHome = { navController.navigate(NavigationScreen.Home.route){
                popUpTo(NavigationScreen.Splash.route){ inclusive = true }
            } }
            val onNextOperation = { navController.navigate(NavigationScreen.Operation.route){
                popUpTo(NavigationScreen.Splash.route){ inclusive = true }
            } }
            SplashScreen().Screen(
                params = SplashScreen.Params(onNextHome, onNextOperation),
                navigationScreen = NavigationScreen.Splash,
                activity = activity
            )
        }
        composable(NavigationScreen.Home.route) {
            HomeScreen().Screen(
                params = HomeScreen.Params { navController.navigate(NavigationScreen.Operation.route){
                    popUpTo(NavigationScreen.Home.route){ inclusive = true }
                } },
                navigationScreen = NavigationScreen.Home,
                activity = activity
            )
        }
        composable(NavigationScreen.Operation.route) {
            val onNexResult = { result : ResultDTO?, transaction : TransactionDTO? ->
                if (result==null){
                    val jsonTransaction = Uri.encode(Gson().toJson(transaction))
                    navController.navigate(NavigationScreen.Result.route.plus("?transaction=${jsonTransaction}"))
                }else{
                    val jsonResult = Uri.encode(Gson().toJson(result))
                    navController.navigate(NavigationScreen.Result.route.plus("?result=${jsonResult}"))
                }
            }
            val onBackHome = { navController.navigate(NavigationScreen.Home.route) }
            OperationScreen().Screen(
                params = OperationScreen.Params (onNexResult, onBackHome),
                navigationScreen = NavigationScreen.Operation,
                activity = activity
            )
        }
        composable(NavigationScreen.Result.route.plus("?result={result}&transaction={transaction}"),
            arguments = mutableListOf(
                navArgument("result") {
                    type = ResultDTO.NavigationType
                    nullable = true
                },
                navArgument("transaction") {
                    type = TransactionDTO.NavigationType
                    nullable = true
                }
            )
        ) { backStack ->
            val result = backStack.arguments?.getParcelable<ResultDTO>("result")
            val transaction = backStack.arguments?.getParcelable<TransactionDTO>("transaction")
            ResultScreen().Screen(
                params = ResultScreen.Params(result, transaction){
                    navController.popBackStack()
                },
                navigationScreen = NavigationScreen.Result,
                activity = activity
            )
        }
    }
}