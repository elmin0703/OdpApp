package com.example.odpapp.ui.theme.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = viewModel(factory = StatisticsViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                StatisticsCard(title = "Statistika Stanovništva po Entitetima") {
                    if (uiState.populationByEntity.isNotEmpty()) {
                        BarChart(data = uiState.populationByEntity)
                    } else {
                        Text("Nema podataka za prikaz.", modifier = Modifier.padding(16.dp))
                    }
                }
            }

            item {
                StatisticsCard(title = "Distribucija Registriranih Vozila") {
                    if (uiState.vehicleTypeDistribution.isNotEmpty() && uiState.vehicleTypeDistribution.sumOf { it.value.toDouble() } > 0) {
                        DonutChart(data = uiState.vehicleTypeDistribution)
                    } else {
                        Text("Nema podataka za prikaz.", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun StatisticsCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun BarChart(data: List<ChartData>) {
    val maxValue = data.maxOfOrNull { it.value } ?: 1f
    val colors = listOf(
        Color(0xFF42A5F5),
        Color(0xFF66BB6A),
        Color(0xFFFFA726)
    )

    Column(modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth()) {
        Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            val barWidth = size.width / (data.size * 2)
            data.forEachIndexed { index, chartData ->
                val barHeight = (chartData.value / maxValue) * size.height
                drawRect(
                    color = colors.getOrElse(index) { chartData.color },
                    topLeft = Offset(x = (index * 2 + 0.5f) * barWidth, y = size.height - barHeight),
                    size = Size(width = barWidth, height = barHeight)
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            data.forEachIndexed { index, chartData ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(colors.getOrElse(index) { chartData.color })
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "${chartData.label} (${chartData.value.toInt()})",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun DonutChart(data: List<ChartData>) {
    val totalValue = data.sumOf { it.value.toDouble() }.toFloat()
    val holeRadiusRatio = 0.6f

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(vertical = 16.dp)) {
        Box(modifier = Modifier.size(150.dp), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize().height(150.dp)) {
                val strokeWidth = size.width * (1 - holeRadiusRatio)
                var startAngle = -90f
                data.forEach { chartData ->
                    val sweepAngle = (chartData.value / totalValue) * 360f
                    // Iscrtavanje luka sa debljinom
                    drawArc(
                        color = chartData.color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = strokeWidth)
                    )
                    // Iscrtavanje crvene linije između isječaka
                    drawArc(
                        color = Color.Red,
                        startAngle = startAngle + sweepAngle - 0.5f,
                        sweepAngle = 1f, // Debljina linije
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                    )
                    startAngle += sweepAngle
                }
            }
            // Tekst u sredini
            Text(
                text = "Ukupno\n${totalValue.toInt()}",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.height(40.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            data.forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(it.color, CircleShape)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "${it.label}: ${it.value.toInt()} (${"%.1f".format((it.value / totalValue) * 100)}%)",
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}