package com.example.partsasign1.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.partsasign1.data.remote.model.OtProgramadaDto
import com.example.partsasign1.data.remote.repository.RepuestosRemoteRepository
import com.example.partsasign1.data.remote.repository.OtRemoteRepository
import com.example.partsasign1.model.OtProgramada
import com.example.partsasign1.model.toDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PendingViewModel : ViewModel() {
    private val otRepo = OtRemoteRepository()
    private val repuestoRepo = RepuestosRemoteRepository()

    private val _pendingOts = MutableStateFlow(emptyList<OtProgramada>())
    val pendingOts: StateFlow<List<OtProgramada>> = _pendingOts.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _actionLoading = MutableStateFlow(false)
    val actionLoading: StateFlow<Boolean> = _actionLoading.asStateFlow()

    private val _actionError = MutableStateFlow<String?>(null)
    val actionError: StateFlow<String?> = _actionError.asStateFlow()

    private val _actionMessage = MutableStateFlow<String?>(null)
    val actionMessage: StateFlow<String?> = _actionMessage.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val repuestos = repuestoRepo.listar()
                val repuestoPorId = repuestos.associateBy { (it.id ?: 0).toString() }

                _pendingOts.value = otRepo.listar().map { dto ->
                    val repuestoNombre = repuestoPorId[dto.repuestoId]?.nombre
                    dto.toDomain(repuestoNombre)
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar pendientes"
                _pendingOts.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }

    fun guardarOt(payload: OtProgramadaPayload, onSuccess: () -> Unit = {}) {
        if (!payload.isValid()) {
            _actionError.value = "Completa los campos obligatorios"
            return
        }

        _actionLoading.value = true
        _actionError.value = null
        viewModelScope.launch {
            try {
                if (payload.id.isNullOrBlank()) {
                    otRepo.crear(payload.toDto())
                    _actionMessage.value = "OT creada"
                } else {
                    val idInt = payload.id.toIntOrNull()
                        ?: throw IllegalArgumentException("ID invalido")
                    otRepo.actualizar(idInt, payload.toDto())
                    _actionMessage.value = "OT actualizada"
                }
                refresh()
                onSuccess()
            } catch (e: Exception) {
                _actionError.value = e.message ?: "No se pudo guardar la OT"
            } finally {
                _actionLoading.value = false
            }
        }
    }

    fun eliminarOt(id: String, onSuccess: () -> Unit = {}) {
        val idInt = id.toIntOrNull()
        if (idInt == null) {
            _actionError.value = "ID invalido"
            return
        }
        _actionLoading.value = true
        _actionError.value = null
        viewModelScope.launch {
            try {
                otRepo.eliminar(idInt)
                refresh()
                onSuccess()
                _actionMessage.value = "OT eliminada"
            } catch (e: Exception) {
                _actionError.value = e.message ?: "No se pudo eliminar la OT"
            } finally {
                _actionLoading.value = false
            }
        }
    }

    fun obtenerPorRepuesto(repuestoId: String): OtProgramada? =
        _pendingOts.value.firstOrNull { it.repuestoId == repuestoId }

    fun clearActionState() {
        _actionError.value = null
        _actionMessage.value = null
    }

    fun marcarRecibido(@Suppress("UNUSED_PARAMETER") repuestoId: String) {
        // Compatibilidad con llamadas existentes: refrescamos la lista desde backend
        refresh()
    }
}

data class OtProgramadaPayload(
    val id: String? = null,
    val nombreTecnico: String,
    val apellidoTecnico: String,
    val rutTecnico: String,
    val numeroEquipo: String,
    val fechaFirma: String,
    val repuestoId: String
) {
    fun isValid(): Boolean =
        nombreTecnico.isNotBlank() &&
                apellidoTecnico.isNotBlank() &&
                rutTecnico.isNotBlank() &&
                numeroEquipo.isNotBlank() &&
                fechaFirma.isNotBlank() &&
                repuestoId.isNotBlank()
}

private fun OtProgramadaPayload.toDto(): OtProgramadaDto = OtProgramadaDto(
    id = id?.toIntOrNull(),
    nombreTecnico = nombreTecnico.trim(),
    apellidoTecnico = apellidoTecnico.trim(),
    rutTecnico = rutTecnico.trim(),
    numeroEquipo = numeroEquipo.trim(),
    fechaFirma = fechaFirma.trim(),
    repuestoId = repuestoId.trim()
)
