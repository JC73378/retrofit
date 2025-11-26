package com.example.partsasign1.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.partsasign1.model.Repuesto
import androidx.lifecycle.viewModelScope
import com.example.partsasign1.data.remote.model.toDomain
import com.example.partsasign1.data.remote.repository.RepuestosRemoteRepository
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val _repuestoDetail = MutableStateFlow<Repuesto?>(null)
    val repuestoDetail: StateFlow<Repuesto?> = _repuestoDetail.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val repo = RepuestosRemoteRepository()

    /** Carga desde el catálogo por ID */
    fun loadById(id: String) {
        val idInt = id.toIntOrNull()
        if (idInt == null) {
            _error.value = "ID inválido"
            _repuestoDetail.value = null
            return
        }
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                _repuestoDetail.value = repo.obtener(idInt).toDomain()
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar repuesto"
                _repuestoDetail.value = null
            } finally {
                _loading.value = false
            }
        }
    }

    /** Carga desde el catálogo por código 365 */
    fun loadByCodigo(codigo365: String) {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val lista = repo.listar().toDomain()
                _repuestoDetail.value = lista.find { it.codigo365 == codigo365 }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar repuesto"
                _repuestoDetail.value = null
            } finally {
                _loading.value = false
            }
        }
    }

    /** Permite inyectar un repuesto ya obtenido externamente */
    fun set(repuesto: Repuesto?) {
        _repuestoDetail.value = repuesto
    }

    fun clear() {
        _repuestoDetail.value = null
    }
}
