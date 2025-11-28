package com.example.partsasign1.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.partsasign1.data.remote.model.RepuestoDto
import com.example.partsasign1.data.remote.model.toDomain
import com.example.partsasign1.data.remote.repository.RepuestosRemoteRepository
import com.example.partsasign1.model.Repuesto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repo = RepuestosRemoteRepository()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchType = MutableStateFlow("Nombre")
    val searchType: StateFlow<String> = _searchType.asStateFlow()

    private val _searchResults = MutableStateFlow(emptyList<Repuesto>())
    val searchResults: StateFlow<List<Repuesto>> = _searchResults.asStateFlow()

    private val _catalog = MutableStateFlow(emptyList<Repuesto>())

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _actionLoading = MutableStateFlow(false)
    val actionLoading: StateFlow<Boolean> = _actionLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

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
                _catalog.value = repo.listar().toDomain()
                performSearch()
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar repuestos"
                _searchResults.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query.replace("\n", "").replace("\r", "")
        performSearch()
    }

    fun updateSearchType(type: String) {
        _searchType.value = type
        performSearch()
    }

    private fun performSearch() {
        val q = _searchQuery.value.trim().lowercase()
        if (q.isEmpty()) {
            _searchResults.value = _catalog.value
            return
        }

        val data = _catalog.value
        _searchResults.value = data.filter { r ->
            when (_searchType.value) {
                "Nombre" -> r.nombre.lowercase().contains(q)
                "Codigo" -> r.codigo365.lowercase().contains(q) || r.codigoBarras.lowercase().contains(q)
                "Ubicacion" -> r.ubicacion.lowercase().contains(q)
                else -> r.nombre.lowercase().contains(q) ||
                        r.codigo365.lowercase().contains(q) ||
                        r.codigoBarras.lowercase().contains(q) ||
                        r.ubicacion.lowercase().contains(q)
            }
        }
    }

    fun crearRepuesto(payload: RepuestoPayload) {
        if (!payload.isValid()) {
            _actionError.value = "Completa los campos obligatorios"
            return
        }
        runCrud {
            repo.crear(payload.copy(id = null).toDto())
            _actionMessage.value = "Repuesto creado"
        }
    }

    fun actualizarRepuesto(payload: RepuestoPayload) {
        val idInt = payload.id?.toIntOrNull()
        if (idInt == null) {
            _actionError.value = "ID invalido"
            return
        }
        if (!payload.isValid()) {
            _actionError.value = "Completa los campos obligatorios"
            return
        }
        runCrud {
            repo.actualizar(idInt, payload.toDto())
            _actionMessage.value = "Repuesto actualizado"
        }
    }

    fun eliminarRepuesto(id: String) {
        val idInt = id.toIntOrNull()
        if (idInt == null) {
            _actionError.value = "ID invalido"
            return
        }
        runCrud {
            repo.eliminar(idInt)
            _actionMessage.value = "Repuesto eliminado"
        }
    }

    fun clearActionMessages() {
        _actionError.value = null
        _actionMessage.value = null
    }

    private fun runCrud(block: suspend () -> Unit) {
        _actionLoading.value = true
        _actionError.value = null
        viewModelScope.launch {
            try {
                block()
                reloadCatalog()
            } catch (e: Exception) {
                _actionError.value = e.message ?: "Error al procesar la solicitud"
            } finally {
                _actionLoading.value = false
            }
        }
    }

    private suspend fun reloadCatalog() {
        _catalog.value = repo.listar().toDomain()
        performSearch()
    }
}

data class RepuestoPayload(
    val id: String? = null,
    val codigo365: String,
    val nombre: String,
    val codigoBarras: String,
    val stockActual: Int,
    val ubicacion: String,
    val categoria: String
) {
    fun isValid(): Boolean =
        codigo365.isNotBlank() &&
                nombre.isNotBlank() &&
                codigoBarras.isNotBlank() &&
                ubicacion.isNotBlank() &&
                categoria.isNotBlank()
}

private fun RepuestoPayload.toDto(): RepuestoDto = RepuestoDto(
    id = id?.toIntOrNull(),
    codigo365 = codigo365.trim(),
    nombre = nombre.trim(),
    codigoBarras = codigoBarras.trim(),
    stockActual = stockActual,
    ubicacion = ubicacion.trim(),
    categoria = categoria.trim()
)
