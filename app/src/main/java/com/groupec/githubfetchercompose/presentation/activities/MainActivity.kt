package com.groupec.githubfetchercompose.presentation.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.groupec.githubfetchercompose.presentation.navigation.*
import com.groupec.githubfetchercompose.presentation.theme.MainTheme
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.groupec.githubfetchercompose.R
import com.groupec.githubfetchercompose.data.dto.RepositoryDTO
import com.groupec.githubfetchercompose.presentation.theme.Green
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var appBarTitle : String ? = null

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
        val currentScreen = getCurrentScreen(navController) ?: NavigationScreen.Home

        //  listen for changes in the navigation destination
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            // val userId = arguments?.getInt("userId")
            if (currentScreen.route==NavigationScreen.Home.route) {
                val repository = arguments?.getParcelable<RepositoryDTO>("repository")
                appBarTitle = repository?.full_name
            }
        }

        Scaffold(
            topBar = {
                AppBarWithMenu(navController, currentScreen)
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

        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Green,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Color.White
            ),
            title = { Text(text = appBarTitle?:stringResource(R.string.app_name_title), fontSize = 18.sp) },
            navigationIcon = {
                if (currentScreen.route==NavigationScreen.Detail.route){
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            },
            actions = {
                if (currentScreen.route==NavigationScreen.Home.route){
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        DropdownMenuItem(onClick = {
                            expanded = false
                        }) {
                            Text(stringResource(R.string.action_settings), color = Color.Black)
                        }
                    }
                }
            }
        )
    }

    @Composable
    private fun getCurrentScreen(navController: NavController): NavigationScreen? {
        return when (navController.currentDestination?.route?.substringBeforeLast("/")?.substringBeforeLast("?")) {
            NavigationScreen.Home.route -> NavigationScreen.Home
            NavigationScreen.Detail.route -> NavigationScreen.Detail
            else -> null
        }
    }
}