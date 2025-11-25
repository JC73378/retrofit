package com.example.partsasign1.viewmodels


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


data class Ubicacion(
    val nombre: String,
    val cantidad: Int
)

class MapViewModel : ViewModel() {
    private val _ubicaciones = MutableStateFlow(emptyList<Ubicacion>())
    val ubicaciones: StateFlow<List<Ubicacion>> = _ubicaciones.asStateFlow()

    init {
        loadUbicaciones()
    }

    private fun loadUbicaciones() {
        _ubicaciones.value = listOf(
            Ubicacion("Almacén A - Estante 4B", 8),
            Ubicacion("Almacén B - Estante 2A", 12),
            Ubicacion("Almacén C - Estante 1C", 5)
        )
    }
}