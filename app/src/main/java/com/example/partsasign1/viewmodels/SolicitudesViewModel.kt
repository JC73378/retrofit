package com.example.partsasign1.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date


data class SolicitudRepuesto(
    val codigo: String,
    val tipo: String,
    val cantidad: Int,
    val descripcion: String,
    val nombreTecnico: String,
    val fecha: Date = Date()
)

class SolicitudesViewModel : ViewModel() {

    private val _historial = MutableStateFlow<List<SolicitudRepuesto>>(emptyList())
    val historial: StateFlow<List<SolicitudRepuesto>> = _historial.asStateFlow()

    fun agregar(solicitud: SolicitudRepuesto) {
        _historial.value = _historial.value + solicitud
    }

}