package com.example.odpapp.ui.theme.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.odpapp.data.FavoriteResidenceEntity
import com.example.odpapp.data.FavoriteVehicleEntity

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = viewModel(factory = FavoritesViewModel.Factory)
) {
    val favoriteResidences by viewModel.favoriteResidences.collectAsState()
    val favoriteVehicles by viewModel.favoriteVehicles.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "Favoriti - Prebivališta",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        if (favoriteResidences.isEmpty()) {
            item { Text("Nema sačuvanih prebivališta.") }
        } else {
            items(favoriteResidences) { residence ->
                FavoriteResidenceCard(residence = residence, onClick = {
                    navController.navigate("datasetRowDetail/residence/${residence.id}")
                })
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(
                "Favoriti - Vozila",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        if (favoriteVehicles.isEmpty()) {
            item { Text("Nema sačuvanih vozila.") }
        } else {
            items(favoriteVehicles) { vehicle ->
                FavoriteVehicleCard(vehicle = vehicle, onClick = {
                    navController.navigate("datasetRowDetail/vehicles/${vehicle.id}")
                })
            }
        }
    }
}

@Composable
fun FavoriteResidenceCard(residence: FavoriteResidenceEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Opština: ${residence.municipality ?: "N/A"}", fontWeight = FontWeight.Bold)
            Text("Institucija: ${residence.institution}")
            Text("Ukupno: ${residence.total}")
        }
    }
}

@Composable
fun FavoriteVehicleCard(vehicle: FavoriteVehicleEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Mjesto registracije: ${vehicle.mjestoReg}", fontWeight = FontWeight.Bold)
            Text("Godina/Mjesec: ${vehicle.year}/${vehicle.month}")
            Text("Ukupno: ${vehicle.total}")
        }
    }
}