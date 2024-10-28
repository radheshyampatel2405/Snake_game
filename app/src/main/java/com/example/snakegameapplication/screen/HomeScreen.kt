package com.example.snakegameapplication.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.snakegameapplication.R
import com.example.snakegameapplication.common.Route

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(modifier : Modifier = Modifier,navController : NavController)
{
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                                                          ),
                title = { Text("Snake Game") }
                     ) },
            )
    { innerPadding ->

        Column(modifier = modifier.fillMaxSize(),
               horizontalAlignment = Alignment.CenterHorizontally,
               verticalArrangement = Arrangement.Center
              )
        {

            OutlinedButton(onClick = { navController.navigate(Route.SnakeScreen) })
            {

                Text(text = "Start Game",
                     style = MaterialTheme.typography.headlineMedium,
                     color = MaterialTheme.colorScheme.primary,
                     modifier = Modifier.size(width = 150.dp, height = 50.dp))
            }

        }

    }

}

//  High score
// start button
// ext button
