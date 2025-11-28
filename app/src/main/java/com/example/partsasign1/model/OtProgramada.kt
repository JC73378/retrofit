package com.example.partsasign1.model

import com.example.partsasign1.data.remote.model.OtProgramadaDto

data class OtProgramada(
    val id: String,
    val repuestoId: String,
    val repuestoNombre: String?,
    val nombreTecnico: String,
    val apellidoTecnico: String,
    val rutTecnico: String,
    val numeroEquipo: String,
    val fechaFirma: String
) {
    val nombreCompletoTecnico: String
        get() = listOf(nombreTecnico, apellidoTecnico)
            .filter { it.isNotBlank() }
            .joinToString(" ")
}

fun OtProgramadaDto.toDomain(repuestoNombre: String? = null): OtProgramada =
    OtProgramada(
        id = (id ?: 0).toString(),
        repuestoId = repuestoId,
        repuestoNombre = repuestoNombre,
        nombreTecnico = nombreTecnico,
        apellidoTecnico = apellidoTecnico,
        rutTecnico = rutTecnico,
        numeroEquipo = numeroEquipo,
        fechaFirma = fechaFirma
    )
