package com.example.odpapp

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.odpapp.ui.theme.screens.HomeViewModel
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.odpapp.navigation.NavGraph
import com.example.odpapp.core.events.TokenEvents   // 👈 DODANO

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OdpApp() {
    val odpViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
    val navController = rememberNavController()
    val currentBackStack = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack.value?.destination?.route

    // 👇 Snackbar host
    val snackbarHostState = remember { SnackbarHostState() }

    // 👇 Slušaj globalni event "token istekao" i prikaži Snackbar
    LaunchedEffect(Unit) {
        TokenEvents.expired.collect {
            snackbarHostState.showSnackbar(
                message = "API token je istekao. Osvježi token ili se ponovo prijavi.",
                withDismissAction = true
            )
            // (opcionalno) automatska navigacija npr. na "home" ili "login":
            // navController.navigate("home") { popUpTo(0) }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }, // 👈 DODANO
        topBar = {
            if (currentRoute != "splash" && currentRoute != "home") {
                OdpTopAppBarv1(navController)
            }
            if (currentRoute == "home")
                OdpTopAppBarv2(navController)
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            homeViewModel = odpViewModel,
            innerPadding = innerPadding
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OdpTopAppBarv1(navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.clickable { navController.navigate("home") }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = "Logo",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(80.dp).padding(end = 8.dp)
                )
                Text(
                    text = "ODP Portal",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color(0xFF003366),
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF003366)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color(0xFF003366)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OdpTopAppBarv2(navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.clickable { navController.navigate("home") }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = "Logo",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(80.dp).padding(end = 8.dp)
                )
                Text(
                    text = "ODP Portal",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color(0xFF003366),
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color(0xFF003366)
        )
    )
}
