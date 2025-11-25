package com.example.partsasign1.ui.Screen
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.partsasign1.viewmodels.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepuestoDetailScreen(
    viewModel: DetailViewModel,
    onBack: () -> Unit,

) {
    val repuesto by viewModel.repuestoDetail.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle Del Repuesto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            repuesto?.let { item ->
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        DetailRow("Nombre:", item.nombre)
                        DetailRow("Código 365:", item.codigo365)
                        DetailRow("Código de barras:", item.codigoBarras)
                        DetailRow("Stock actual:", item.stockActual.toString())
                        DetailRow("Ubicación:", item.ubicacion)
                        DetailRow("Categoría:", item.categoria)
                    }
                }
            } ?: run {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("No se encontró información del repuesto.")
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text("$label ", style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
