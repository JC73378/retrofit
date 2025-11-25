package com.example.partsasign1.data.remote.model

data class RepuestoDto(
    val id: Int? = null,
    val codigo365: String,
    val nombre: String,
    val codigoBarras: String,
    val stockActual: Int,
    val ubicacion: String,
    val categoria: String
)

data class OtProgramadaDto(
    val id: Int? = null,
    val nombreTecnico: String,
    val apellidoTecnico: String,
    val rutTecnico: String,
    val numeroEquipo: String,
    val fechaFirma: String, // yyyy-MM-dd
    val repuestoId: String
)

data class RecepcionDto(
    val id: Int? = null,
    val repuestoId: String,
    val cantidadRecibida: Int,
    val recibidoPor: String,
    val fechaRecepcion: String, // yyyy-MM-dd
    val observacion: String? = null
)

data class UsuarioDto(
    val id: Int? = null,
    val username: String,
    val password: String,
    val rol: String,
    val activo: Boolean = true
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val rol: String
)
