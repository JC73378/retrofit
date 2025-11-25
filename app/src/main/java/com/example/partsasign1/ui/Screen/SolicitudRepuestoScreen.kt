package com.example.partsasign1.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.partsasign1.viewmodels.SolicitudRepuesto

// IMPORTACIONES PARA NotificaciónES
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
// Permisos DE NOTIFICACIONES
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitudRepuestoScreen(
    onBack: () -> Unit,
    onGuardado: (SolicitudRepuesto) -> Unit
) {
    var codigo by rememberSaveable { mutableStateOf("") }
    var nombreTecnico by rememberSaveable { mutableStateOf("") }
    var tipoRepuesto by rememberSaveable { mutableStateOf("") }
    var cantidad by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }

    var showErrors by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val tipos = listOf("Eléctrico", "Mecánico", "Hidráulico", "Neumático", "Otros")
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Solicitar repuesto") },
                navigationIcon = {
                    IconButton(onClick = onBack, enabled = !isLoading) {
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
                value = codigo,
                onValueChange = { codigo = it },
                label = { Text("Código de repuesto *") },
                singleLine = true,
                isError = showErrors && codigo.isBlank(),
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nombreTecnico,
                onValueChange = { nombreTecnico = it },
                label = { Text("Nombre del técnico *") },
                singleLine = true,
                isError = showErrors && nombreTecnico.isBlank(),
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it && !isLoading }
            ) {
                OutlinedTextField(
                    value = tipoRepuesto,
                    onValueChange = { tipoRepuesto = it },
                    label = { Text("Tipo de repuesto *") },
                    readOnly = true,
                    isError = showErrors && tipoRepuesto.isBlank(),
                    enabled = !isLoading,
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
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción (motivo) *") },
                minLines = 3,
                isError = showErrors && descripcion.isBlank(),
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
                    enabled = !isLoading,
                    modifier = Modifier.weight(1f)
                ) { Text("Cancelar") }

                Button(
                    onClick = {
                        val ok = codigo.isNotBlank() &&
                                nombreTecnico.isNotBlank() &&
                                tipoRepuesto.isNotBlank() &&
                                cantidad.isNotBlank() &&
                                descripcion.isNotBlank()
                        if (!ok) { showErrors = true; return@Button }

                        isLoading = true
                        scope.launch {
                            delay(1800)
                            isLoading = false


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
                            } else {

                            }

                            onGuardado(
                                SolicitudRepuesto(
                                    codigo = codigo,
                                    tipo = tipoRepuesto,
                                    cantidad = cantidad.toInt(),
                                    descripcion = descripcion,
                                    nombreTecnico = nombreTecnico
                                )
                            )
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier.weight(1f)
                ) {
                    if (isLoading) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Cargando repuesto...")
                        }
                    } else {
                        Text("Guardar")
                    }
                }
            }
        }
    }

    if (isLoading) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {},
            title = { Text("Cargando repuesto") },
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