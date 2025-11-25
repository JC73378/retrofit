package com.example.partsasign1.domain.Validation


import android.util.Patterns

//validaciones del correo: formato y no este vacio
fun validateEmail(email: String): String?{
    if(email.isBlank()) return "El correo es obligatorio"
    val ok = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    return if(!ok) "Formato de correo Inválido" else null
}

//Validaciones de rut para recibir un repuesto
fun rutValido(input: String): Boolean {
    val clean = input.replace(".", "").uppercase()
    if (!Regex("""^\d{7,8}-[\dK]$""").matches(clean)) return false
    val partes = clean.split("-")
    val num = partes[0]
    val dv = partes[1]
    return calcularDV(num) == dv
}
fun calcularDV(numero: String): String {
    var suma = 0
    var factor = 2
    for (i in numero.length - 1 downTo 0) {
        suma += (numero[i].code - '0'.code) * factor
        factor = if (factor == 7) 2 else factor + 1
    }
    val resto = 11 - (suma % 11)
    return when (resto) {
        11 -> "0"
        10 -> "K"
        else -> resto.toString()
    }
}