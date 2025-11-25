package com.example.partsasign1.ui.Screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.partsasign1.viewmodels.PendingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingRScreen(
    viewModel: PendingViewModel,
    onRepuestoClick: (String) -> Unit,
    onBack: () -> Unit,
    onNavigateToOTProgramada: (String) -> Unit
) {
    val pendingRepuestos by viewModel.pendingRepuestos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Repuestos por Recibir") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Atrás") } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (pendingRepuestos.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.Green,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text("No hay repuestos pendientes", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(pendingRepuestos) { repuesto ->
                        Card(
                            onClick = { onRepuestoClick(repuesto.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(repuesto.nombre, style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(4.dp))
                                Text("Código: ${repuesto.codigo365}", style = MaterialTheme.typography.bodyMedium)
                                Text("Ubicación: ${repuesto.ubicacion}", style = MaterialTheme.typography.bodySmall)
                                Spacer(Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        onNavigateToOTProgramada(repuesto.id)
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Marcar como Recibido")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}