package com.example.partsasign1.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.partsasign1.model.Repuesto
import com.example.partsasign1.data.local.repository.CatalogRepository

class DetailViewModel : ViewModel() {

    private val _repuestoDetail = MutableStateFlow<Repuesto?>(null)
    val repuestoDetail: StateFlow<Repuesto?> = _repuestoDetail.asStateFlow()

    /** Carga desde el catálogo por ID */
    fun loadById(id: String) {
        _repuestoDetail.value = CatalogRepository.porId(id)
    }

    /** Carga desde el catálogo por código 365 */
    fun loadByCodigo(codigo365: String) {
        _repuestoDetail.value = CatalogRepository.porCodigo(codigo365)
    }

    /** Permite inyectar un repuesto ya obtenido externamente */
    fun set(repuesto: Repuesto?) {
        _repuestoDetail.value = repuesto
    }

    fun clear() {
        _repuestoDetail.value = null
    }
}