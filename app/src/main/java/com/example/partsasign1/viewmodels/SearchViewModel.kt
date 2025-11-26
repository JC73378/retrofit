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
        if (q.length < 1) { _searchResults.value = _catalog.value; return }

        val data = _catalog.value
        _searchResults.value = data.filter { r ->
            when (_searchType.value) {
                "Nombre" -> r.nombre.lowercase().contains(q)
                "Código" -> r.codigo365.lowercase().contains(q) || r.codigoBarras.lowercase().contains(q)
                "Ubicación" -> r.ubicacion.lowercase().contains(q)
                else -> r.nombre.lowercase().contains(q) ||
                        r.codigo365.lowercase().contains(q) ||
                        r.codigoBarras.lowercase().contains(q) ||
                        r.ubicacion.lowercase().contains(q)
            }
        }
    }
}
