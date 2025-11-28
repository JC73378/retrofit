package com.example.partsasign1.ui.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.partsasign1.viewmodels.SolicitudRepuesto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteRepuestoScreen(
    repuestoId: String,
    repuestoNombre: String,
    onBack: () -> Unit,
    onGuardado: (SolicitudRepuesto) -> Unit
) {
    var nombreTecnico by rememberSaveable { mutableStateOf("") }
    var motivo by rememberSaveable { mutableStateOf("") }

    var showErrors by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Solicitud de baja") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
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
            Text("Repuesto: $repuestoNombre")
            Text("Codigo: $repuestoId")

            OutlinedTextField(
                value = nombreTecnico,
                onValueChange = { nombreTecnico = it },
                label = { Text("Nombre del solicitante *") },
                singleLine = true,
                isError = showErrors && nombreTecnico.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = motivo,
                onValueChange = { motivo = it },
                label = { Text("Motivo / descripci\u00f3n *") },
                minLines = 3,
                isError = showErrors && motivo.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                ) { Text("Cancelar") }

                Button(
                    onClick = {
                        val ok = nombreTecnico.isNotBlank() && motivo.isNotBlank()
                        if (!ok) {
                    showErrors = true
                    return@Button
                }
                onGuardado(
                    SolicitudRepuesto(
                        codigo = repuestoId,
                        tipo = "Eliminacion",
                        cantidad = 1,
                        descripcion = motivo,
                        nombreTecnico = nombreTecnico
                    )
                )
            },
                    modifier = Modifier.weight(1f)
                ) { Text("Guardar solicitud") }
            }
        }
    }
}
