package com.example.partsasign1.viewmodels

import androidx.lifecycle.ViewModel
import com.example.partsasign1.model.Repuesto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date

class PendingViewModel : ViewModel() {
    private val _pendingRepuestos = MutableStateFlow(emptyList<Repuesto>())
    val pendingRepuestos: StateFlow<List<Repuesto>> = _pendingRepuestos.asStateFlow()

    init {

        if (_pendingRepuestos.value.isEmpty()) {
            loadPendingRepuestos()
        }
    }

    private fun loadPendingRepuestos() {
        _pendingRepuestos.value = listOf(
            Repuesto(
                id = "1",
                codigo365 = "RPT-001",
                nombre = "Motor Eléctrico 5HP",
                codigoBarras = "1234567890123",
                stockActual = 0,
                ubicacion = "Almacén A - Estante 4B"
            ),
            Repuesto(
                id = "2",
                codigo365 = "RPT-002",
                nombre = "Rodamiento SKF 6205",
                codigoBarras = "1234567890124",
                stockActual = 0,
                ubicacion = "Almacén B - Estante 2A"
            )
        )
    }

    fun marcarRecibido(repuestoId: String) {
        _pendingRepuestos.value = _pendingRepuestos.value.filter { it.id != repuestoId }
    }
}