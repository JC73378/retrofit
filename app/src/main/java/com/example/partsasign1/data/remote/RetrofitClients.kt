package com.example.partsasign1.data.remote

import com.example.partsasign1.data.remote.api.OtApi
import com.example.partsasign1.data.remote.api.AuthApi
import com.example.partsasign1.data.remote.api.RepuestosApi
import com.example.partsasign1.data.remote.api.SolicitudesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_REPUESTOS = "http://ii4qadk5i0.loclx.io/"  // inventario-repuestos-ms
private const val BASE_SOLICITUDES = "http://ei22gj2hos.loclx.io/"       // microservicio de solicitudes
private const val BASE_OT = "http://omr17tqal3.loclx.io/"  // recepciones-repuestos-ms
private const val BASE_AUTH = "http://rnhxmo60ev.loclx.io/"       // auth-usuarios-ms

private fun retrofit(baseUrl: String): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

object RetrofitClients {
    val repuestosApi: RepuestosApi by lazy { retrofit(BASE_REPUESTOS).create(RepuestosApi::class.java) }
    val otApi: OtApi by lazy { retrofit(BASE_OT).create(OtApi::class.java) }
    val solicitudesApi: SolicitudesApi by lazy { retrofit(BASE_SOLICITUDES).create(SolicitudesApi::class.java) }
    val authApi: AuthApi by lazy { retrofit(BASE_AUTH).create(AuthApi::class.java) }
}
