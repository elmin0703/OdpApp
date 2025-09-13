package com.example.odpapp.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.odpapp.data.ResidenceEntity
import com.example.odpapp.data.VehicleEntity
import com.example.odpapp.model.ResidenceData
import com.example.odpapp.model.VehicleData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatasetDetailScreen(
    datasetDetailViewModel: DatasetDetailViewModel = viewModel(factory = DatasetDetailViewModel.Factory),
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // Sakupljamo stanje iz StateFlow-a
    val uiState by datasetDetailViewModel.uiState.collectAsState()

    // State variables
    var selectedEntity by rememberSaveable { mutableStateOf("") }
    var selectedCanton by rememberSaveable { mutableStateOf("") }
    var searchMunicipality by rememberSaveable { mutableStateOf("") }
    var expandedEntity by rememberSaveable { mutableStateOf(false) }
    var expandedCanton by rememberSaveable { mutableStateOf(false) }
    var sortField by rememberSaveable { mutableStateOf("entity") }
    var sortAscending by rememberSaveable { mutableStateOf(true) }
    var currentPage by remember { mutableIntStateOf(0) }
    val rowsPerPage = 10


    Scaffold(
    ) { innerPadding ->
        when (val currentState = uiState) {
            is DatasetDetailUiState.Loading -> LoadingScreen()
            is DatasetDetailUiState.Error -> ErrorScreen(retryAction = { datasetDetailViewModel.loadDatasetDetails() })
            is DatasetDetailUiState.Success -> {
                val allData = currentState.data

                // Filter data
                val filteredData = allData.filter { data ->
                    (selectedEntity.isEmpty() || data.getEntity() == selectedEntity) &&
                            (selectedCanton.isEmpty() || data.getCanton() == selectedCanton) &&
                            (searchMunicipality.isEmpty() || data.getMunicipality().contains(searchMunicipality, true))
                }

                // Sort data
                val sortedData = when (sortField) {
                    "entity" -> filteredData.sortedBy { it.getEntity() }
                    "canton" -> filteredData.sortedBy { it.getCanton() }
                    "municipality" -> filteredData.sortedBy { it.getMunicipality() }
                    "total" -> filteredData.sortedBy { it.getTotal() }
                    else -> filteredData
                }.let { if (sortAscending) it else it.reversed() }

                // Pagination
                val totalPages = (sortedData.size + rowsPerPage - 1) / rowsPerPage
                val pageData = sortedData.drop(currentPage * rowsPerPage).take(rowsPerPage)

                // Get unique values1 for dropdowns
                val allEntities = listOf("") + allData.mapNotNull { it.getEntity() }.distinct()
                val allCantons = listOf("") + allData.mapNotNull { it.getCanton() }.distinct()

                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text("Filtering podatko:", style = MaterialTheme.typography.titleMedium)
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Entity dropdown
                            ExposedDropdownMenuBox(
                                expanded = expandedEntity,
                                onExpandedChange = { expandedEntity = !expandedEntity },
                                modifier = Modifier.weight(1f)
                            ) {
                                TextField(
                                    value = selectedEntity,
                                    onValueChange = {},
                                    label = { Text("Entitet") },
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedEntity) },
                                    modifier = Modifier.menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedEntity,
                                    onDismissRequest = { expandedEntity = false }
                                ) {
                                    allEntities.forEach { entity ->
                                        DropdownMenuItem(
                                            text = { Text(entity.ifEmpty { "Svi entiteti" }) },
                                            onClick = {
                                                selectedEntity = entity
                                                expandedEntity = false
                                                currentPage = 0
                                            }
                                        )
                                    }
                                }
                            }

                            // Canton dropdown
                            ExposedDropdownMenuBox(
                                expanded = expandedCanton,
                                onExpandedChange = { expandedCanton = !expandedCanton },
                                modifier = Modifier.weight(1f)
                            ) {
                                TextField(
                                    value = selectedCanton,
                                    onValueChange = {},
                                    label = { Text("Kanton") },
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedCanton) },
                                    modifier = Modifier.menuAnchor(),
                                    enabled = selectedEntity.isNotEmpty()
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedCanton,
                                    onDismissRequest = { expandedCanton = false }
                                ) {
                                    allCantons.forEach { canton ->
                                        DropdownMenuItem(
                                            text = { Text(canton.ifEmpty { "Svi kantoni" }) },
                                            onClick = {
                                                selectedCanton = canton
                                                expandedCanton = false
                                                currentPage = 0
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = searchMunicipality,
                            onValueChange = { searchMunicipality = it },
                            label = { Text("Opština") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        Text("Sorting po:", style = MaterialTheme.typography.titleMedium)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            listOf("Entitet", "Kanton", "Opština", "Ukupno").forEach { option ->
                                val field = option.lowercase()
                                FilterChip(
                                    selected = sortField == field,
                                    onClick = {
                                        if (sortField == field) {
                                            sortAscending = !sortAscending
                                        } else {
                                            sortField = field
                                            sortAscending = true
                                        }
                                        currentPage = 0
                                    },
                                    label = {
                                        Text("$option ${if (sortField == field) (if (sortAscending) "↑" else "↓") else ""}")
                                    }
                                )
                            }
                        }
                    }

                    item {
                        when (allData.firstOrNull()) {
                            is ResidenceEntity -> ResidenceDataTable(navController, pageData.filterIsInstance<ResidenceEntity>())
                            is VehicleEntity -> VehicleDataTable(navController, pageData.filterIsInstance<VehicleEntity>())
                            else -> Text("Nema podataka za prikaz")
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { currentPage = (currentPage - 1).coerceAtLeast(0) },
                                enabled = currentPage > 0
                            ) {
                                Text("Prethodna")
                            }

                            Text("Strana ${currentPage + 1} od $totalPages")

                            Button(
                                onClick = { currentPage = (currentPage + 1).coerceAtMost(totalPages - 1) },
                                enabled = currentPage < totalPages - 1
                            ) {
                                Text("Sljedeća")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResidenceDataTable(
    navController: NavHostController,
    data: List<ResidenceEntity>,
    modifier: Modifier = Modifier
) {
    val horizontalScrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
    ) {
        // Table header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .horizontalScroll(horizontalScrollState)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TableHeader("Entitet", 120.dp)
            TableHeader("Kanton", 100.dp)
            TableHeader("Opština", 120.dp)
            TableHeader("Institucija", 120.dp)
            TableHeader("Datum ažuriranja", 120.dp)
            TableHeader("Sa trajnim preb.", 120.dp)
            TableHeader("Sa privremenim preb.", 120.dp)
            TableHeader("Ukupno", 80.dp)
        }

        // Table content
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            data.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            navController.navigate("datasetRowDetail/residence/${item.id}")
                        }
                        .horizontalScroll(horizontalScrollState),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TableCell(item.entitet, 120.dp)
                    TableCell(item.canton ?: "-", 100.dp)
                    TableCell(item.municipality.toString(), 120.dp)
                    TableCell(item.institution, 120.dp)
                    TableCell(item.dateUpdate, 120.dp)
                    TableCell(item.saTrajPreb.toString(), 120.dp)
                    TableCell(item.saPrivPreb.toString(), 120.dp)
                    TableCell(item.total.toString(), 80.dp)
                }
                Divider()
            }
        }
    }
}

@Composable
private fun VehicleDataTable(
    navController: NavHostController,
    data: List<VehicleEntity>,
    modifier: Modifier = Modifier
) {
    val horizontalScrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
    ) {
        // Table header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .horizontalScroll(horizontalScrollState)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TableHeader("Entitet", 120.dp)
            TableHeader("Kanton", 100.dp)
            TableHeader("Opština", 120.dp)
            TableHeader("Godina", 80.dp)
            TableHeader("Mjesec", 80.dp)
            TableHeader("Datum ažuriranja", 120.dp)
            TableHeader("Mjesto registracije", 120.dp)
            TableHeader("Domaća vozila", 100.dp)
            TableHeader("Strana vozila", 100.dp)
            TableHeader("Ukupno", 80.dp)
        }

        // Table content
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            data.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            navController.navigate("datasetRowDetail/vehicles/${item.id}")
                        }
                        .horizontalScroll(horizontalScrollState),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TableCell(item.entity ?: "", 120.dp)
                    TableCell(item.canton ?: "-", 100.dp)
                    TableCell(item.municipality.toString(), 120.dp)
                    TableCell(item.year.toString(), 80.dp)
                    TableCell(item.month.toString(), 80.dp)
                    TableCell(item.dateUpdate, 120.dp)
                    TableCell(item.mjestoReg, 120.dp)
                    TableCell(item.domVoz.toString(), 100.dp)
                    TableCell(item.strVoz.toString(), 100.dp)
                    TableCell(item.total.toString(), 80.dp)
                }
                Divider()
            }
        }
    }
}

@Composable
private fun TableHeader(text: String, width: Dp) {
    Box(
        modifier = Modifier
            .width(width)
            .padding(4.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun TableCell(text: String, width: Dp) {
    Box(
        modifier = Modifier
            .width(width)
            .padding(4.dp)
    ) {
        Text(text)
    }
}


private fun Any.getEntity(): String = when (this) {
    is ResidenceData -> entity
    is VehicleData -> entity
    is ResidenceEntity -> entitet
    is VehicleEntity -> entity ?: ""
    else -> ""
}

private fun Any.getCanton(): String = when (this) {
    is ResidenceData -> canton ?: ""
    is VehicleData -> canton ?: ""
    is ResidenceEntity -> canton ?: ""
    is VehicleEntity -> canton ?: ""
    else -> ""
}

private fun Any.getMunicipality(): String = when (this) {
    is ResidenceData -> municipality
    is VehicleData -> municipality
    is ResidenceEntity -> municipality ?: ""
    is VehicleEntity -> municipality ?: ""
    else -> ""
}.toString()

private fun Any.getTotal(): Int = when (this) {
    is ResidenceData -> total
    is VehicleData -> total
    is ResidenceEntity -> total
    is VehicleEntity -> total
    else -> 0
}