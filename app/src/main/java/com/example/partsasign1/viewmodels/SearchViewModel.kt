package com.example.partsasign1.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.partsasign1.model.Repuesto

class SearchViewModel : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchType = MutableStateFlow("Nombre")
    val searchType: StateFlow<String> = _searchType.asStateFlow()

    private val _searchResults = MutableStateFlow(emptyList<Repuesto>())
    val searchResults: StateFlow<List<Repuesto>> = _searchResults.asStateFlow()

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
        if (q.length < 1) { _searchResults.value = emptyList(); return }

        val data = com.example.partsasign1.data.local.repository.CatalogRepository.listar()
        _searchResults.value = data.filter { r ->
            r.nombre.lowercase().contains(q) ||
                    r.codigo365.lowercase().contains(q) ||
                    r.codigoBarras.lowercase().contains(q) ||
                    r.ubicacion.lowercase().contains(q)
        }
    }
}