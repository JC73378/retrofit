package com.example.partsasign1.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    val pendingOts by viewModel.pendingOts.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val actionLoading by viewModel.actionLoading.collectAsState()
    val actionError by viewModel.actionError.collectAsState()
    val actionMessage by viewModel.actionMessage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Repuestos por Recibir") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Atras") } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            when {
                loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(12.dp))
                        Text("Cargando pendientes...")
                    }
                }

                error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(error ?: "Error inesperado")
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = viewModel::refresh) {
                            Text("Reintentar")
                        }
                    }
                }

                pendingOts.isEmpty() -> {
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
                }

                else -> {
                    actionError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    actionMessage?.let {
                        Text(
                            text = it,
                            color = Color.Green,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(pendingOts) { ot ->
                            Card(
                                onClick = { onRepuestoClick(ot.repuestoId) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(Modifier.padding(16.dp)) {
                                    Text(
                                        ot.repuestoNombre ?: "Repuesto ${ot.repuestoId}",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text("OT #${ot.id}", style = MaterialTheme.typography.bodySmall)
                                    Text("Tecnico: ${ot.nombreCompletoTecnico}", style = MaterialTheme.typography.bodyMedium)
                                    Text("Rut: ${ot.rutTecnico}", style = MaterialTheme.typography.bodySmall)
                                    Text("Equipo: ${ot.numeroEquipo}", style = MaterialTheme.typography.bodySmall)
                                    Text("Fecha firma: ${ot.fechaFirma}", style = MaterialTheme.typography.bodySmall)
                                    Spacer(Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = { onNavigateToOTProgramada(ot.repuestoId) },
                                            modifier = Modifier.weight(1f),
                                            enabled = !actionLoading
                                        ) {
                                            Text("Ver / editar")
                                        }
                                        Button(
                                            onClick = { viewModel.eliminarOt(ot.id) },
                                            modifier = Modifier.weight(1f),
                                            enabled = !actionLoading
                                        ) {
                                            Text("Eliminar")
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
}
