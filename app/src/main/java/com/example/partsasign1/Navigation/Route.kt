package com.example.partsasign1.Navigation

// Sealed class para definir las rutas de forma segura
sealed class Route(val path: String) {
    data object Login      : Route("login")
    data object Dashboard  : Route("dashboard")
    data object Search     : Route("search")
    data object Scanner    : Route("scanner")
    data object Pending    : Route("pending")
    data object Profile    : Route("profile")
    data object Map        : Route("map")
    data object Detail     : Route("detail/{id}")

    data object Solicitud  : Route("solicitud")

    data object Historial : Route("historial_repuestos")

    data object OTProgramada : Route("ot_programada/{id}")
}