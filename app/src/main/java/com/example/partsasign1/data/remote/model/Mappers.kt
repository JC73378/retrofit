package com.example.partsasign1.data.remote.model

import com.example.partsasign1.model.Repuesto

/** Mapea DTOs remotos al modelo de dominio usado en la UI */
fun RepuestoDto.toDomain(): Repuesto =
    Repuesto(
        id = (id ?: 0).toString(),
        codigo365 = codigo365,
        nombre = nombre,
        codigoBarras = codigoBarras,
        stockActual = stockActual,
        ubicacion = ubicacion,
        categoria = categoria
    )

fun List<RepuestoDto>.toDomain(): List<Repuesto> = map { it.toDomain() }
