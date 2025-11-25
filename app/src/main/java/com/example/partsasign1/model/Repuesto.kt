package com.example.partsasign1.model

data class Repuesto(
    val id: String,
    val codigo365: String,
    val nombre: String,
    val codigoBarras: String,
    val stockActual: Int,
    val ubicacion: String,
    val categoria: String = "General"
)