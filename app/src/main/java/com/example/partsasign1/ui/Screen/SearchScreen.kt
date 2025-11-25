package com.example.partsasign1.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.FilterChip
import com.example.partsasign1.viewmodels.SearchViewModel
import com.example.partsasign1.model.Repuesto // ← Agrega esta importación
import kotlin.collections.forEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onRepuestoClick: (String) -> Unit,
    onBack: () -> Unit,
    onCreateSolicitud: () -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchType by viewModel.searchType.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscar Repuestos") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Atrás") }
                },
                actions = {

                    IconButton(onClick = onCreateSolicitud) {
                        Icon(Icons.Default.Add, contentDescription = "Nueva solicitud")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Search Header
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it.filterNot { c -> c == '\n' || c == '\r' }) },
                label = { Text("Buscar repuesto") },
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            // Search Filters
            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                listOf("Nombre", "Código", "Ubicación").forEach { type ->
                    FilterChip(
                        selected = searchType == type,
                        onClick = { viewModel.updateSearchType(type) },
                        label = { Text(type) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            // Results
            if (searchResults.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("No se encontraron resultados")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(searchResults) { repuesto ->
                        RepuestoItem(
                            repuesto = repuesto,
                            onClick = { onRepuestoClick(repuesto.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RepuestoItem(repuesto: Repuesto, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(repuesto.nombre, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Código: ${repuesto.codigo365}", style = MaterialTheme.typography.bodyMedium)
            Text("Ubicación: ${repuesto.ubicacion}", style = MaterialTheme.typography.bodySmall)
            Text("Stock: ${repuesto.stockActual}", style = MaterialTheme.typography.bodySmall)
        }
    }
}