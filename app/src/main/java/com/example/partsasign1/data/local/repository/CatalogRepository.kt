package com.example.partsasign1.data.local.repository

import com.example.partsasign1.model.Repuesto

object CatalogRepository {

    //Catalogo para Repuestos sera llamado al usar el search
    private val catalogo = listOf(
        Repuesto("1", "RPT-001", "Bomba hidráulica 2HP", "7801234567890", 12, "A-1"),
        Repuesto("2", "RPT-002", "Rodamiento 6203", "7800987654321", 80, "B-3"),
        Repuesto("3", "RPT-003", "Correa A45", "7801112223334", 25, "C-2"),
        Repuesto("4", "RPT-004", "Sensor Proximidad M12", "7804445556667", 10, "D-5"),
        Repuesto("5", "RPT-005", "Contactores 18A", "7807778889990", 7, "E-4"),
        Repuesto("6", "RPT-006", "Ventilador 24 V", "7808090828833", 8, "A-1"),
        Repuesto("7", "RPT-007", "Cilindro de aire 2000", "780774838290", 5, "E-4"),
        Repuesto("8", "RPT-008", "Placa de circuito integrado", "78077755490", 7, "B-3"),
        Repuesto("9", "RPT-009", "Control AISAI", "7807393929990", 2, "C-2")
    )

    fun listar(): List<Repuesto> = catalogo
    fun codigos(): List<String> = catalogo.map { it.codigo365 }
    fun porId(id: String): Repuesto? = catalogo.find { it.id == id }
    fun porCodigo(codigo: String): Repuesto? = catalogo.find { it.codigo365 == codigo }
}