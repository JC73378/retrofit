package com.example.partsasign1.data.remote

import com.example.partsasign1.data.remote.api.AuthApi
import com.example.partsasign1.data.remote.api.OtApi
import com.example.partsasign1.data.remote.api.RecepcionApi
import com.example.partsasign1.data.remote.api.RepuestosApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_REPUESTOS = "http://10.0.2.2:9112/"  // inventario-repuestos-ms
private const val BASE_OT = "http://10.0.2.2:9103/"         // ot-programadas-ms
private const val BASE_RECEPCION = "http://10.0.2.2:9102/"  // recepciones-repuestos-ms
private const val BASE_AUTH = "http://10.0.2.2:9111/"       // auth-usuarios-ms

private fun retrofit(baseUrl: String): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

object RetrofitClients {
    val repuestosApi: RepuestosApi by lazy { retrofit(BASE_REPUESTOS).create(RepuestosApi::class.java) }
    val otApi: OtApi by lazy { retrofit(BASE_OT).create(OtApi::class.java) }
    val recepcionApi: RecepcionApi by lazy { retrofit(BASE_RECEPCION).create(RecepcionApi::class.java) }
    val authApi: AuthApi by lazy { retrofit(BASE_AUTH).create(AuthApi::class.java) }
}
