package com.example.odpapp.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.odpapp.data.ResidenceEntity
import com.example.odpapp.data.VehicleEntity
import android.content.Intent
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.platform.LocalContext
import com.example.odpapp.data.toResidenceEntity
import com.example.odpapp.data.toVehicleEntity

@Composable
fun DatasetRowDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DatasetRowDetailViewModel = viewModel(
        factory = DatasetRowDetailViewModel.Factory
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val context = LocalContext.current

    Box(modifier = modifier.fillMaxSize().padding(16.dp)) {
        when (val state = uiState) {
            is RowDetailUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is RowDetailUiState.Error -> {
                Text(text = "Greška: podaci nisu pronađeni.", modifier = Modifier.align(Alignment.Center))
            }
            is RowDetailUiState.Success -> {
                Column {
                    // Red s gumbima na vrhu
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // GUMB ZA DIJELJENJE
                        IconButton(onClick = {
                            val shareText = createShareText(state.data)
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Podijeli putem")
                            context.startActivity(shareIntent)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Podijeli",
                                tint = Color.Gray,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        // GUMB ZA FAVORITE
                        IconButton(onClick = { viewModel.toggleFavorite() }) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                contentDescription = "Dodaj u favorite",
                                tint = if (isFavorite) Color(0xFFFFD700) else Color.Gray,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Prikaz detalja
                    when (val data = state.data) {
                        is ResidenceEntity -> ResidenceDetails(data)
                        is VehicleEntity -> VehicleDetails(data)
                    }
                }
            }
        }
    }
}

@Composable
private fun ResidenceDetails(data: ResidenceEntity) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        DetailRow("Entitet:", data.entitet)
        DetailRow("Kanton:", data.canton)
        DetailRow("Opština:", data.municipality)
        DetailRow("Institucija:", data.institution)
        DetailRow("Datum ažuriranja:", data.dateUpdate)
        DetailRow("Sa trajnim prebivalištem:", data.saTrajPreb.toString())
        DetailRow("Sa privremenim prebivalištem:", data.saPrivPreb.toString())
        DetailRow("Ukupno:", data.total.toString())
    }
}

@Composable
private fun VehicleDetails(data: VehicleEntity) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        DetailRow("Entitet:", data.entity)
        DetailRow("Kanton:", data.canton)
        DetailRow("Opština:", data.municipality)
        DetailRow("Godina:", data.year.toString())
        DetailRow("Mjesec:", data.month.toString())
        DetailRow("Datum ažuriranja:", data.dateUpdate)
        DetailRow("Mjesto registracije:", data.mjestoReg)
        DetailRow("Domaća vozila:", data.domVoz.toString())
        DetailRow("Strana vozila:", data.strVoz.toString())
        DetailRow("Ukupno:", data.total.toString())
    }
}

@Composable
private fun DetailRow(label: String, value: String?) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value ?: "N/A",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Divider()
    }
}

private fun createShareText(data: Any): String {
    val builder = StringBuilder("Podaci sa ODP Portala BiH:\n\n")
    when (data) {
        is ResidenceEntity -> {
            builder.append("PREBIVALIŠTE\n")
            builder.append("--------------------\n")
            builder.append("Entitet: ${data.entitet}\n")
            data.municipality?.let { builder.append("Opština: $it\n") }
            builder.append("Institucija: ${data.institution}\n")
            builder.append("Ukupno osoba: ${data.total}\n")
            builder.append("Datum ažuriranja: ${data.dateUpdate}")
        }
        is VehicleEntity -> {
            builder.append("REGISTROVANA VOZILA\n")
            builder.append("--------------------\n")
            data.entity?.let { builder.append("Entitet: $it\n") }
            data.municipality?.let { builder.append("Opština: $it\n") }
            builder.append("Godina/Mjesec: ${data.year}/${data.month}\n")
            builder.append("Ukupno vozila: ${data.total}\n")
            builder.append("Datum ažuriranja: ${data.dateUpdate}")
        }
    }
    return builder.toString()
}