package com.example.partsasign1.ui.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.partsasign1.viewmodels.SolicitudesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialRepuestosScreen(
    viewModel: SolicitudesViewModel,
    onBack: () -> Unit
) {
    val historial by viewModel.historial.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val actionLoading by viewModel.actionLoading.collectAsState()
    val actionError by viewModel.actionError.collectAsState()
    val actionMessage by viewModel.actionMessage.collectAsState()

    var editing by remember { mutableStateOf<com.example.partsasign1.viewmodels.SolicitudRepuesto?>(null) }
    var deleteId by remember { mutableStateOf<Int?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(actionError) {
        actionError?.let { snackbarHostState.showSnackbar(it); viewModel.clearActionMessages() }
    }
    LaunchedEffect(actionMessage) {
        actionMessage?.let { snackbarHostState.showSnackbar(it); viewModel.clearActionMessages() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de solicitudes") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        when {
            loading -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
            error != null -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(error ?: "Error al cargar solicitudes")
                        Button(onClick = viewModel::refrescar) { Text("Reintentar") }
                    }
                }
            }
            historial.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Sin solicitudes registradas.")
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(historial.asReversed()) { s ->
                        Card {
                            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Fecha: ${s.fecha ?: "-"}", style = MaterialTheme.typography.bodyMedium)
                                Text("Codigo: ${s.codigo}")
                                Text("Tipo: ${s.tipo}")
                                Text("Cantidad: ${s.cantidad}")
                                Text("Tecnico: ${s.nombreTecnico}")
                                Text("Motivo: ${s.descripcion}")
                                Spacer(Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedButton(onClick = { editing = s }, enabled = !actionLoading) {
                                        Icon(Icons.Default.Edit, contentDescription = null)
                                        Spacer(Modifier.padding(horizontal = 4.dp))
                                        Text("Editar")
                                    }
                                    OutlinedButton(onClick = { deleteId = s.id }, enabled = !actionLoading) {
                                        Icon(Icons.Default.Delete, contentDescription = null)
                                        Spacer(Modifier.padding(horizontal = 4.dp))
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

    editing?.let { sol ->
        SolicitudEditDialog(
            titulo = "Editar solicitud",
            initial = sol,
            onDismiss = { editing = null },
            onSave = { updated ->
                viewModel.actualizar(updated.copy(id = sol.id))
                editing = null
            }
        )
    }

    deleteId?.let { id ->
        AlertDialog(
            onDismissRequest = { deleteId = null },
            title = { Text("Eliminar solicitud") },
            text = { Text("¿Seguro que deseas eliminar esta solicitud?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminar(id)
                    deleteId = null
                }, enabled = !actionLoading) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { deleteId = null }, enabled = !actionLoading) { Text("Cancelar") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SolicitudEditDialog(
    titulo: String,
    initial: com.example.partsasign1.viewmodels.SolicitudRepuesto,
    onDismiss: () -> Unit,
    onSave: (com.example.partsasign1.viewmodels.SolicitudRepuesto) -> Unit
) {
    var codigo by rememberSaveable { mutableStateOf(initial.codigo) }
    var tipo by rememberSaveable { mutableStateOf(initial.tipo) }
    var cantidad by rememberSaveable { mutableStateOf(initial.cantidad.toString()) }
    var descripcion by rememberSaveable { mutableStateOf(initial.descripcion) }
    var nombre by rememberSaveable { mutableStateOf(initial.nombreTecnico) }
    var showErrors by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(titulo) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = codigo,
                    onValueChange = { codigo = it },
                    label = { Text("Codigo") },
                    singleLine = true,
                    isError = showErrors && codigo.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = tipo,
                    onValueChange = { tipo = it },
                    label = { Text("Tipo") },
                    singleLine = true,
                    isError = showErrors && tipo.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { input -> if (input.all { it.isDigit() }) cantidad = input },
                    label = { Text("Cantidad") },
                    singleLine = true,
                    isError = showErrors && cantidad.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripcion") },
                    minLines = 2,
                    isError = showErrors && descripcion.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre tecnico") },
                    singleLine = true,
                    isError = showErrors && nombre.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val cantidadInt = cantidad.toIntOrNull()
                val ok = codigo.isNotBlank() &&
                        tipo.isNotBlank() &&
                        cantidadInt != null &&
                        descripcion.isNotBlank() &&
                        nombre.isNotBlank()
                if (!ok) {
                    showErrors = true
                    return@TextButton
                }
                onSave(
                    initial.copy(
                        codigo = codigo,
                        tipo = tipo,
                        cantidad = cantidadInt ?: 0,
                        descripcion = descripcion,
                        nombreTecnico = nombre
                    )
                )
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
