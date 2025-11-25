package com.example.partsasign1.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.partsasign1.viewmodels.DashboardViewModel
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration

import com.example.partsasign1.viewmodels.SolicitudesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToSearch: () -> Unit,
    onNavigateToScanner: () -> Unit,
    onNavigateToPending: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToHistorial: () -> Unit,
    solicitudesVM: SolicitudesViewModel
) {
    val stats by viewModel.stats.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Gestión de Repuestos") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (isLandscape) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatsCard("Disponibles", stats.disponibles, Color(0xFF4CAF50), Modifier.weight(1f))
                    Spacer(Modifier.width(8.dp))
                    StatsCard("Retirados",   stats.retirados,   Color(0xFFFF9800), Modifier.weight(1f))
                    Spacer(Modifier.width(8.dp))
                    StatsCard("Por Recibir", stats.pendientes,  Color(0xFFF44336), Modifier.weight(1f))
                    Spacer(Modifier.width(8.dp))
                    StatsCard("Total",       stats.total,       Color(0xFF2196F3), Modifier.weight(1f))
                }
            } else {
                Row(modifier = Modifier.padding(16.dp)) {
                    StatsCard("Disponibles", stats.disponibles, Color(0xFF4CAF50), Modifier.weight(1f))
                    Spacer(Modifier.width(16.dp))
                    StatsCard("Retirados",   stats.retirados,   Color(0xFFFF9800), Modifier.weight(1f))
                }
                Row(modifier = Modifier.padding(16.dp)) {
                    StatsCard("Por Recibir", stats.pendientes,  Color(0xFFF44336), Modifier.weight(1f))
                    Spacer(Modifier.width(16.dp))
                    StatsCard("Total",       stats.total,       Color(0xFF2196F3), Modifier.weight(1f))
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "Acciones Rápidas",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )

            if (isLandscape) {
                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        ActionButton(Icons.Default.Search,  "Buscar Repuesto",        onNavigateToSearch)
                        ActionButton(Icons.Default.Camera,  "Escanear Código",        onNavigateToScanner)
                        ActionButton(Icons.Default.Person,  "Mi Perfil",              onNavigateToProfile)
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        ActionButton(Icons.Default.Schedule,"Por Recibir",            onNavigateToPending)
                        ActionButton(Icons.Default.Place,   "Ver Mapa Ubicaciones",   onNavigateToMap)
                        ActionButton(Icons.Default.History, "Historial de solicitudes", onNavigateToHistorial)
                    }
                }
            } else {
                Column {
                    ActionButton(Icons.Default.Search,   "Buscar Repuesto",        onNavigateToSearch)
                    ActionButton(Icons.Default.Camera,   "Escanear Código",        onNavigateToScanner)
                    ActionButton(Icons.Default.Schedule, "Por Recibir",            onNavigateToPending)
                    ActionButton(Icons.Default.Person,   "Mi Perfil",              onNavigateToProfile)
                    ActionButton(Icons.Default.Place,    "Ver Mapa Ubicaciones",   onNavigateToMap)
                    ActionButton(Icons.Default.History,  "Historial de solicitudes", onNavigateToHistorial)
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun StatsCard(
    title: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}