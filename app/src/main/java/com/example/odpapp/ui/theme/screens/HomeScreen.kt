package com.example.odpapp.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.odpapp.model.Dataset
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeUiState: HomeUiState,
    retryAction: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Scaffold(
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                "Dostupni Datasetovi:",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            when (homeUiState) {
                is HomeUiState.Loading -> LoadingScreen(modifier.size(40.dp))
                is HomeUiState.Error -> ErrorScreen(retryAction = retryAction, modifier)
                is HomeUiState.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = modifier.fillMaxSize()
                    ) {
                        items(homeUiState.data.size) { index ->
                            val dataset = homeUiState.data[index]
                            DatasetCard(
                                dataset = dataset,
                                onClick = {
                                    navController.navigate("datasetDetails/${dataset.id}")
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 10.dp),
                                shape = RoundedCornerShape(20.dp),
                                elevation = CardDefaults.cardElevation(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFE7ECF5)),
                                onClick = { navController.navigate("favorites") } // Navigacija na favorites
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Moji Favoriti",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                color = Color(0xFF003366),
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Text(
                                            text = "Pregledajte sačuvane podatke",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = Color(0xFF5A5A5A)
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 10.dp),
                                shape = RoundedCornerShape(20.dp),
                                elevation = CardDefaults.cardElevation(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFE7ECF5)),
                                onClick = { navController.navigate("statistics") } // Navigacija na statistiku
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Vizualizacija Podataka",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                color = Color(0xFF003366),
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Text(
                                            text = "Pogledajte grafički prikaz podataka",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = Color(0xFF5A5A5A)
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 10.dp),
                                shape = RoundedCornerShape(20.dp),
                                elevation = CardDefaults.cardElevation(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFE7ECF5)),
                                onClick = { navController.navigate("apiAuth") }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "API autentifikacija",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                color = Color(0xFF003366),
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Error loading data")
        Button(onClick = retryAction) {
            Text("Retry")
        }
    }
}

@Composable
fun DatasetCard(
    dataset: Dataset,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardColor = Color(0xFFE7ECF5)

    Card(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 10.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = dataset.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color(0xFF003366),
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = dataset.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF5A5A5A)
                    )
                )
            }
        }
    }
}