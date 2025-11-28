package com.example.partsasign1.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.partsasign1.domain.Validation.rutValido
import com.example.partsasign1.viewmodels.OtProgramadaPayload
import com.example.partsasign1.viewmodels.PendingViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTProgramadaScreen(
    repuestoId: String,
    viewModel: PendingViewModel,
    onBack: () -> Unit,
    onOTGuardada: (String) -> Unit = {}
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var apellido by rememberSaveable { mutableStateOf("") }
    var rut by rememberSaveable { mutableStateOf("") }
    var numeroEquipo by rememberSaveable { mutableStateOf("") }

    val loadingListado by viewModel.loading.collectAsState()
    val actionLoading by viewModel.actionLoading.collectAsState()
    val actionError by viewModel.actionError.collectAsState()
    val pendingOts by viewModel.pendingOts.collectAsState()

    val sdf = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    var fechaFirmaMillis by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showErrors by remember { mutableStateOf(false) }

    val otExistente = pendingOts.firstOrNull { it.repuestoId == repuestoId }
    val isProcessing = loadingListado || actionLoading

    LaunchedEffect(repuestoId) {
        viewModel.refresh()
        viewModel.clearActionState()
        nombre = ""
        apellido = ""
        rut = ""
        numeroEquipo = ""
        fechaFirmaMillis = System.currentTimeMillis()
    }

    LaunchedEffect(otExistente?.id) {
        otExistente?.let { ot ->
            nombre = ot.nombreTecnico
            apellido = ot.apellidoTecnico
            rut = ot.rutTecnico
            numeroEquipo = ot.numeroEquipo
            fechaFirmaMillis = try {
                sdf.parse(ot.fechaFirma)?.time ?: System.currentTimeMillis()
            } catch (_: Exception) {
                System.currentTimeMillis()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("OT Programada") },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        enabled = !isProcessing
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atras")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del tecnico *") },
                singleLine = true,
                isError = showErrors && nombre.isBlank(),
                enabled = !isProcessing,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = apellido,
                onValueChange = { apellido = it },
                label = { Text("Apellido *") },
                singleLine = true,
                isError = showErrors && apellido.isBlank(),
                enabled = !isProcessing,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = rut,
                onValueChange = { rut = it.uppercase() },
                label = { Text("RUT (con guion) *") },
                placeholder = { Text("12.345.678-9") },
                singleLine = true,
                isError = showErrors && !rutValido(rut),
                enabled = !isProcessing,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = numeroEquipo,
                onValueChange = { if (it.all { c -> c.isDigit() }) numeroEquipo = it },
                label = { Text("Numero de equipo *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = showErrors && numeroEquipo.isBlank(),
                enabled = !isProcessing,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = sdf.format(Date(fechaFirmaMillis)),
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha de firma *") },
                trailingIcon = {
                    TextButton(
                        onClick = { showDatePicker = true },
                        enabled = !isProcessing
                    ) { Text("Cambiar") }
                },
                isError = showErrors && fechaFirmaMillis <= 0L,
                enabled = !isProcessing,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f),
                    enabled = !isProcessing
                ) { Text("Cancelar") }

                Button(
                    onClick = {
                        val ok = nombre.isNotBlank() &&
                                apellido.isNotBlank() &&
                                rutValido(rut) &&
                                numeroEquipo.isNotBlank()
                        if (!ok) {
                            showErrors = true
                            return@Button
                        }

                        viewModel.guardarOt(
                            OtProgramadaPayload(
                                id = otExistente?.id,
                                nombreTecnico = nombre,
                                apellidoTecnico = apellido,
                                rutTecnico = rut,
                                numeroEquipo = numeroEquipo,
                                fechaFirma = sdf.format(Date(fechaFirmaMillis)),
                                repuestoId = repuestoId
                            )
                        ) {
                            onOTGuardada(repuestoId)
                            onBack()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isProcessing
                ) {

                    if (isProcessing) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Procesando...")
                        }
                    } else {
                        Text("Guardar OT")
                    }
                }
            }

            if (otExistente?.id != null) {
                TextButton(
                    onClick = {
                        val id = otExistente.id
                        viewModel.eliminarOt(id) {
                            onOTGuardada(repuestoId)
                            onBack()
                        }
                    },
                    enabled = !isProcessing
                ) {
                    Text("Eliminar OT")
                }
            }
        }
    }

    if (showDatePicker) {
        val dateState = rememberDatePickerState(initialSelectedDateMillis = fechaFirmaMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        dateState.selectedDateMillis?.let { ms -> fechaFirmaMillis = ms }
                        showDatePicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = dateState)
        }
    }

    actionError?.let { message ->
        AlertDialog(
            onDismissRequest = { viewModel.clearActionState() },
            confirmButton = {
                TextButton(onClick = { viewModel.clearActionState() }) { Text("Entendido") }
            },
            title = { Text("Error") },
            text = { Text(message) }
        )
    }
}
