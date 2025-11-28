package com.example.partsasign1.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.partsasign1.data.remote.model.SolicitudDto
import com.example.partsasign1.data.remote.repository.SolicitudesRemoteRepository
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SolicitudRepuesto(
    val id: Int? = null,
    val codigo: String,
    val tipo: String,
    val cantidad: Int,
    val descripcion: String,
    val nombreTecnico: String,
    val fecha: String? = null // ISO string from backend
)

class SolicitudesViewModel(
    private val repo: SolicitudesRemoteRepository = SolicitudesRemoteRepository()
) : ViewModel() {

    private val _historial = MutableStateFlow<List<SolicitudRepuesto>>(emptyList())
    val historial: StateFlow<List<SolicitudRepuesto>> = _historial.asStateFlow()

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
        refrescar()
    }

    fun refrescar() {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                _historial.value = repo.listar().map { it.toDomain() }
            } catch (e: Exception) {
                _error.value = e.message ?: "No se pudo cargar solicitudes"
            } finally {
                _loading.value = false
            }
        }
    }

    fun agregar(solicitud: SolicitudRepuesto, onSuccess: () -> Unit = {}) {
        runAction(onSuccess = onSuccess) {
            repo.crear(solicitud.toDto())
            _actionMessage.value = "Solicitud guardada"
            refrescar()
        }
    }

    fun actualizar(solicitud: SolicitudRepuesto, onSuccess: () -> Unit = {}) {
        val id = solicitud.id
        if (id == null) {
            _actionError.value = "ID inválido"
            return
        }
        runAction(onSuccess = onSuccess) {
            repo.actualizar(id, solicitud.toDto())
            _actionMessage.value = "Solicitud actualizada"
            refrescar()
        }
    }

    fun eliminar(id: Int, onSuccess: () -> Unit = {}) {
        runAction(onSuccess = onSuccess) {
            repo.eliminar(id)
            _actionMessage.value = "Solicitud eliminada"
            refrescar()
        }
    }

    fun clearActionMessages() {
        _actionError.value = null
        _actionMessage.value = null
    }

    private fun runAction(onSuccess: () -> Unit = {}, block: suspend () -> Unit) {
        _actionLoading.value = true
        _actionError.value = null
        viewModelScope.launch {
            try {
                block()
                onSuccess()
            } catch (e: Exception) {
                _actionError.value = e.message ?: "Error al procesar la solicitud"
            } finally {
                _actionLoading.value = false
            }
        }
    }
}

private fun SolicitudDto.toDomain(): SolicitudRepuesto = SolicitudRepuesto(
    id = id,
    codigo = codigo,
    tipo = tipo,
    cantidad = cantidad,
    descripcion = descripcion,
    nombreTecnico = nombreTecnico,
    fecha = fecha
)

private fun SolicitudRepuesto.toDto(): SolicitudDto = SolicitudDto(
    id = id,
    codigo = codigo,
    tipo = tipo,
    cantidad = cantidad,
    descripcion = descripcion,
    nombreTecnico = nombreTecnico,
    fecha = fecha ?: LocalDate.now().toString()
)
