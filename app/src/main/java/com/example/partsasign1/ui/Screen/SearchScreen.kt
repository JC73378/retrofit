package com.example.partsasign1.ui.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.partsasign1.model.Repuesto
import com.example.partsasign1.viewmodels.RepuestoPayload
import com.example.partsasign1.viewmodels.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onRepuestoClick: (String) -> Unit,
    onBack: () -> Unit,
    onCreateSolicitud: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchType by viewModel.searchType.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val actionLoading by viewModel.actionLoading.collectAsState()
    val actionError by viewModel.actionError.collectAsState()
    val actionMessage by viewModel.actionMessage.collectAsState()

    var showForm by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<Repuesto?>(null) }
    var repuestoToDelete by remember { mutableStateOf<Repuesto?>(null) }

    LaunchedEffect(actionMessage) {
        actionMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearActionMessages()
            if (showForm) {
                showForm = false
                editing = null
            }
        }
    }

    LaunchedEffect(actionError) {
        actionError?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearActionMessages()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { editing = null; showForm = true },
                icon = { Icon(Icons.Default.Add, contentDescription = "Crear repuesto") },
                text = { Text("Crear repuesto") }
            )
        },
        topBar = {
            TopAppBar(
                title = { Text("Buscar Repuestos") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Atras") }
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

            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                listOf("Nombre", "Codigo", "Ubicacion").forEach { type ->
                    FilterChip(
                        selected = searchType == type,
                        onClick = { viewModel.updateSearchType(type) },
                        label = { Text(type) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            when {
                loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(12.dp))
                        Text("Cargando repuestos...")
                    }
                }

                error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(error ?: "Error inesperado")
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = viewModel::refresh) {
                            Text("Reintentar")
                        }
                    }
                }

                searchResults.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("No se encontraron resultados")
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(searchResults) { repuesto ->
                            RepuestoItem(
                                repuesto = repuesto,
                                onClick = { onRepuestoClick(repuesto.id) },
                                onEdit = {
                                    editing = repuesto
                                    showForm = true
                                },
                                onDelete = { repuestoToDelete = repuesto }
                            )
                        }
                    }
                }
            }
        }
    }

    repuestoToDelete?.let { repuesto ->
        AlertDialog(
            onDismissRequest = { repuestoToDelete = null },
            title = { Text("Eliminar repuesto") },
            text = { Text("¿Seguro que deseas eliminar ${repuesto.nombre}? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        repuestoToDelete = null
                        viewModel.eliminarRepuesto(repuesto.id)
                    },
                    enabled = !actionLoading
                ) {
                    if (actionLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Eliminar")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { repuestoToDelete = null }, enabled = !actionLoading) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showForm) {
        RepuestoFormDialog(
            title = if (editing == null) "Nuevo repuesto" else "Editar repuesto",
            initial = editing,
            loading = actionLoading,
            onDismiss = {
                if (!actionLoading) {
                    showForm = false
                    editing = null
                }
            },
            onSubmit = { payload ->
                if (editing == null) {
                    viewModel.crearRepuesto(payload)
                } else {
                    viewModel.actualizarRepuesto(payload.copy(id = editing?.id))
                }
            }
        )
    }
}

@Composable
fun RepuestoItem(
    repuesto: Repuesto,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(repuesto.nombre, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Codigo: ${repuesto.codigo365}", style = MaterialTheme.typography.bodyMedium)
            Text("Ubicacion: ${repuesto.ubicacion}", style = MaterialTheme.typography.bodySmall)
            Text("Stock: ${repuesto.stockActual}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Editar")
                }
                Spacer(Modifier.width(4.dp))
                TextButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Eliminar")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RepuestoFormDialog(
    title: String,
    initial: Repuesto?,
    loading: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (RepuestoPayload) -> Unit
) {
    var codigo365 by rememberSaveable { mutableStateOf(initial?.codigo365 ?: "") }
    var nombre by rememberSaveable { mutableStateOf(initial?.nombre ?: "") }
    var codigoBarras by rememberSaveable { mutableStateOf(initial?.codigoBarras ?: "") }
    var stockActual by rememberSaveable { mutableStateOf(initial?.stockActual?.toString() ?: "") }
    var ubicacion by rememberSaveable { mutableStateOf(initial?.ubicacion ?: "") }
    var categoria by rememberSaveable { mutableStateOf(initial?.categoria ?: "") }
    var showErrors by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { if (!loading) onDismiss() },
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = codigo365,
                    onValueChange = { codigo365 = it },
                    label = { Text("Codigo 365") },
                    singleLine = true,
                    isError = showErrors && codigo365.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    isError = showErrors && nombre.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = codigoBarras,
                    onValueChange = { codigoBarras = it },
                    label = { Text("Codigo de barras") },
                    singleLine = true,
                    isError = showErrors && codigoBarras.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = stockActual,
                    onValueChange = { input -> if (input.all { it.isDigit() }) stockActual = input },
                    label = { Text("Stock actual") },
                    singleLine = true,
                    isError = showErrors && stockActual.isBlank(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = ubicacion,
                    onValueChange = { ubicacion = it },
                    label = { Text("Ubicacion") },
                    singleLine = true,
                    isError = showErrors && ubicacion.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoria") },
                    singleLine = true,
                    isError = showErrors && categoria.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val stockInt = stockActual.toIntOrNull()
                    val valid = codigo365.isNotBlank() &&
                            nombre.isNotBlank() &&
                            codigoBarras.isNotBlank() &&
                            stockInt != null &&
                            ubicacion.isNotBlank() &&
                            categoria.isNotBlank()
                    if (!valid) {
                        showErrors = true
                        return@TextButton
                    }
                    onSubmit(
                        RepuestoPayload(
                            id = initial?.id,
                            codigo365 = codigo365,
                            nombre = nombre,
                            codigoBarras = codigoBarras,
                            stockActual = stockInt ?: 0,
                            ubicacion = ubicacion,
                            categoria = categoria
                        )
                    )
                },
                enabled = !loading
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Guardar")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !loading) {
                Text("Cancelar")
            }
        }
    )
}
