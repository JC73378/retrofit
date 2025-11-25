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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTProgramadaScreen(
    repuestoId: String,
    onBack: () -> Unit,
    onOTGuardada: (String) -> Unit = {}
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var apellido by rememberSaveable { mutableStateOf("") }
    var rut by rememberSaveable { mutableStateOf("") }
    var numeroEquipo by rememberSaveable { mutableStateOf("") }


    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()


    val sdf = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    var fechaFirmaMillis by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showErrors by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("OT Programada") },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        enabled = !isLoading
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
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
                label = { Text("Nombre del técnico *") },
                singleLine = true,
                isError = showErrors && nombre.isBlank(),
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = apellido,
                onValueChange = { apellido = it },
                label = { Text("Apellido *") },
                singleLine = true,
                isError = showErrors && apellido.isBlank(),
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = rut,
                onValueChange = { rut = it.uppercase() },
                label = { Text("RUT (con guión) *") },
                placeholder = { Text("12.345.678-9") },
                singleLine = true,
                isError = showErrors && !rutValido(rut),
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = numeroEquipo,
                onValueChange = { if (it.all { c -> c.isDigit() }) numeroEquipo = it },
                label = { Text("Número de equipo *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = showErrors && numeroEquipo.isBlank(),
                enabled = !isLoading,
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
                        enabled = !isLoading
                    ) { Text("Cambiar") }
                },
                isError = showErrors && fechaFirmaMillis <= 0L,
                enabled = !isLoading,
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
                    enabled = !isLoading
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


                        isLoading = true

                        scope.launch {
                            delay(3000) //simula la carga
                            onOTGuardada(repuestoId) //  Marca como recibido el repuesto
                            isLoading = false
                            onBack() // ← Navegar de regreso
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading
                ) {

                    if (isLoading) {
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
}