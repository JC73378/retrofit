package com.example.partsasign1.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationMapScreen(
    onBack: () -> Unit
) {
    val ubicaciones = listOf(
        Ubicacion("Almacén A - Estante 4B", 8),
        Ubicacion("Almacén B - Estante 2A", 12),
        Ubicacion("Almacén C - Estante 1C", 5)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ubicaciones") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text(
                "Ubicación de Repuestos",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ubicaciones) { ubicacion ->
                    LocationCard(ubicacion = ubicacion)
                }
            }
        }
    }
}

data class Ubicacion(val nombre: String, val cantidad: Int)

@Composable
fun LocationCard(ubicacion: Ubicacion) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.Blue,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(ubicacion.nombre, style = MaterialTheme.typography.titleMedium)
                Text("${ubicacion.cantidad} repuestos", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}