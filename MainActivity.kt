package com.example.carboncopy

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carboncopy.ui.theme.CarbonCopyTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableIntStateOf


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarbonCopyTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen(navController) }
        composable("gameSetup") { GameSetupScreen(navController) }
        composable("game/{players}/{hp}") { backStackEntry ->
            val players = backStackEntry.arguments?.getString("players")?.toInt() ?: 2
            val hp = backStackEntry.arguments?.getString("hp")?.toInt() ?: 20
            GameScreen(navController, players, hp)
        }
        composable("settings") { SettingsScreen(navController) }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Home") })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { navController.navigate("game") }) {
                Text("Start Game")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("settings") }) {
                Text("Settings")
            }
        }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(navController: NavHostController, players: Int, startingHP: Int) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game Screen") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("Players: $players")
            Text("Starting HP: $startingHP")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        // Use padding from Scaffold
        Text(
            text = "Settings functionality will go here!",
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSetupScreen(navController: NavHostController) {
    var players by remember { mutableStateOf(2) }
    var startingHP by remember { mutableStateOf(20) }
    var expandedPlayers by remember { mutableStateOf(false) }
    var expandedHP by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game Setup") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Player Selection Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedPlayers,
                onExpandedChange = { expandedPlayers = it }
            ) {
                TextField(
                    value = "$players Players",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Players") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPlayers) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedPlayers,
                    onDismissRequest = { expandedPlayers = false }
                ) {
                    (2..6).forEach { option ->
                        DropdownMenuItem(
                            text = { Text("$option Players") },
                            onClick = {
                                players = option
                                expandedPlayers = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Starting HP Selection Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedHP,
                onExpandedChange = { expandedHP = it }
            ) {
                TextField(
                    value = "$startingHP HP",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Starting HP") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedHP) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedHP,
                    onDismissRequest = { expandedHP = false }
                ) {
                    listOf(20, 40, 60, 80).forEach { option ->
                        DropdownMenuItem(
                            text = { Text("$option HP") },
                            onClick = {
                                startingHP = option
                                expandedHP = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Start Game Button
            Button(onClick = {
                navController.navigate("game/$players/$startingHP")
            }) {
                Text("Start Game")
            }
        }
    }
}

