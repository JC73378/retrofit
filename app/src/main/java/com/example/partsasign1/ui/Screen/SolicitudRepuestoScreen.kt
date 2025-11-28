package com.example.partsasign1.ui.Screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.partsasign1.viewmodels.SolicitudRepuesto
import com.example.partsasign1.viewmodels.SolicitudesViewModel
import java.time.LocalDate
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitudRepuestoScreen(
    viewModel: SolicitudesViewModel,
    onBack: () -> Unit,
    onGuardado: () -> Unit
) {
    var codigo by rememberSaveable { mutableStateOf("") }
    var nombreTecnico by rememberSaveable { mutableStateOf("") }
    var tipoRepuesto by rememberSaveable { mutableStateOf("") }
    var cantidad by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val tipos = listOf("El\u00e9ctrico", "Mec\u00e1nico", "Hidr\u00e1ulico", "Neum\u00e1tico", "Otros")

    val actionLoading by viewModel.actionLoading.collectAsState()
    val actionError by viewModel.actionError.collectAsState()
    val actionMessage by viewModel.actionMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(actionError) {
        actionError?.let { snackbarHostState.showSnackbar(it); viewModel.clearActionMessages() }
    }
    LaunchedEffect(actionMessage) {
        actionMessage?.let { snackbarHostState.showSnackbar(it); viewModel.clearActionMessages() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Solicitar repuesto") },
                navigationIcon = {
                    IconButton(onClick = onBack, enabled = !actionLoading) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                value = codigo,
                onValueChange = { codigo = it },
                label = { Text("Código de repuesto *") },
                singleLine = true,
                isError = showErrors && codigo.isBlank(),
                enabled = !actionLoading,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nombreTecnico,
                onValueChange = { nombreTecnico = it },
                label = { Text("Nombre del técnico *") },
                singleLine = true,
                isError = showErrors && nombreTecnico.isBlank(),
                enabled = !actionLoading,
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it && !actionLoading }
            ) {
                OutlinedTextField(
                    value = tipoRepuesto,
                    onValueChange = { },
                    label = { Text("Tipo de repuesto *") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    isError = showErrors && tipoRepuesto.isBlank(),
                    enabled = !actionLoading,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    tipos.forEach { t ->
                        DropdownMenuItem(
                            text = { Text(t) },
                            onClick = {
                                tipoRepuesto = t
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = cantidad,
                onValueChange = { input -> if (input.all { it.isDigit() }) cantidad = input },
                label = { Text("Cantidad *") },
                singleLine = true,
                isError = showErrors && cantidad.isBlank(),
                enabled = !actionLoading,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción (motivo) *") },
                minLines = 3,
                isError = showErrors && descripcion.isBlank(),
                enabled = !actionLoading,
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
                    enabled = !actionLoading,
                    modifier = Modifier.weight(1f)
                ) { Text("Cancelar") }

                Button(
                    onClick = {
                        val ok = codigo.isNotBlank() &&
                                nombreTecnico.isNotBlank() &&
                                tipoRepuesto.isNotBlank() &&
                                cantidad.isNotBlank() &&
                                descripcion.isNotBlank()
                        if (!ok) {
                            showErrors = true
                            return@Button
                        }

                        val solicitud = SolicitudRepuesto(
                            codigo = codigo,
                            tipo = tipoRepuesto,
                            cantidad = cantidad.toInt(),
                            descripcion = descripcion,
                            nombreTecnico = nombreTecnico,
                            fecha = LocalDate.now().toString()
                        )
                        viewModel.agregar(solicitud) {
                            val builder = NotificationCompat.Builder(context, "solicitudes_channel")
                                .setSmallIcon(android.R.drawable.ic_menu_send)
                                .setContentTitle("Solicitud enviada")
                                .setContentText("El repuesto $codigo ha sido solicitado correctamente.")
                                .setAutoCancel(true)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                            val hasPermission =
                                Build.VERSION.SDK_INT < 33 ||
                                        ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.POST_NOTIFICATIONS
                                        ) == PackageManager.PERMISSION_GRANTED

                            if (hasPermission &&
                                NotificationManagerCompat.from(context).areNotificationsEnabled()
                            ) {
                                NotificationManagerCompat.from(context).notify(1001, builder.build())
                            }

                            onGuardado()
                        }
                    },
                    enabled = !actionLoading,
                    modifier = Modifier.weight(1f)
                ) {
                    if (actionLoading) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Guardando...")
                        }
                    } else {
                        Text("Guardar")
                    }
                }
            }
        }
    }

    if (actionLoading) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {},
            title = { Text("Guardando solicitud") },
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator()
                    Text("Procesando solicitud...")
                }
            }
        )
    }
}
