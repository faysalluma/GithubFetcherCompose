package com.groupec.githubfetchercompose.presentation.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.groupec.githubfetchercompose.presentation.navigation.*
import com.groupec.githubfetchercompose.presentation.theme.MainTheme
import androidx.compose.material.DropdownMenuItem
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.groupec.githubfetchercompose.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainTheme {
                MainScreen()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        val scope = rememberCoroutineScope()
        val currentScreen = getCurrentScreen(navController) ?: NavigationScreen.Splash
        val configuration = LocalConfiguration.current

        navController.addOnDestinationChangedListener { _, destination, _ ->

        }

        Scaffold(
            topBar = {
                if (currentScreen.route != NavigationScreen.Splash.route) {
                    AppBarWithMenu(navController, currentScreen)
                }
            }
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                NavGraph(navController, Modifier.padding(it), this@MainActivity)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppBarWithMenu(navController: NavHostController, currentScreen: NavigationScreen) {
        var expanded by remember { mutableStateOf(false) }

        TopAppBar(
            title = { Text(text = stringResource(R.string.app_name_title), fontSize = 18.sp) },
            actions = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    DropdownMenuItem(onClick = {
                        if (currentScreen.route==NavigationScreen.Operation.route)
                            navController.navigate(NavigationScreen.Home.route)
                        else{
                            navController.popBackStack()
                            navController.navigate(NavigationScreen.Home.route)
                        }
                        expanded = false
                    }) {
                        Text(stringResource(R.string.action_settings), color = Color.Black)
                    }
                }
            }
        )
    }

    @Composable
    private fun getCurrentScreen(navController: NavController): NavigationScreen? {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        return when (navBackStackEntry?.destination?.route?.substringBeforeLast("/")?.substringBeforeLast("?")) {
            NavigationScreen.Splash.route -> NavigationScreen.Splash
            NavigationScreen.Home.route -> NavigationScreen.Home
            NavigationScreen.Operation.route -> NavigationScreen.Operation
            NavigationScreen.Result.route -> NavigationScreen.Result
            else -> null
        }
    }
}