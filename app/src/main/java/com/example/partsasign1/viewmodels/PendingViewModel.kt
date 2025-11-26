package com.example.partsasign1.viewmodels

import androidx.lifecycle.ViewModel
import com.example.partsasign1.model.Repuesto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.example.partsasign1.data.remote.model.toDomain
import com.example.partsasign1.data.remote.repository.RepuestosRemoteRepository
import kotlinx.coroutines.launch

class PendingViewModel : ViewModel() {
    private val repo = RepuestosRemoteRepository()

    private val _pendingRepuestos = MutableStateFlow(emptyList<Repuesto>())
    val pendingRepuestos: StateFlow<List<Repuesto>> = _pendingRepuestos.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                // Consideramos "pendiente" a los repuestos con stock 0
                _pendingRepuestos.value = repo.listar().toDomain().filter { it.stockActual <= 0 }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar pendientes"
                _pendingRepuestos.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }

    fun marcarRecibido(repuestoId: String) {
        _pendingRepuestos.value = _pendingRepuestos.value.filter { it.id != repuestoId }
    }
}
