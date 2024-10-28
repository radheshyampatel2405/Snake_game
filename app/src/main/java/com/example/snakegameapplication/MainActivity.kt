package com.example.snakegameapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.snakegameapplication.common.Route
import com.example.snakegameapplication.common.SnakeViewModel
import com.example.snakegameapplication.screen.HomeScreen
import com.example.snakegameapplication.screen.SnakeScreen
import com.example.snakegameapplication.ui.theme.SnakeGameApplicationTheme

class MainActivity : ComponentActivity()
{
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SnakeGameApplicationTheme {

                val viewModel = viewModel<SnakeViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                val navController= rememberNavController()
                NavHost(navController = navController, startDestination = "HomeScreen" , builder = {
                    composable(Route.HomeScren){
                        HomeScreen(navController = navController)
                    }
                    composable(Route.SnakeScreen){
                        SnakeScreen(
                            state = state,
                            onEvent = viewModel::onEvent
                                  )
                    }
                }
                       )

            }
        }
    }
}
