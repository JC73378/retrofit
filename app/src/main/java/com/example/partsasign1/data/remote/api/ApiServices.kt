package com.example.partsasign1.data.remote.api

import com.example.partsasign1.data.remote.model.LoginRequest
import com.example.partsasign1.data.remote.model.LoginResponse
import com.example.partsasign1.data.remote.model.OtProgramadaDto
import com.example.partsasign1.data.remote.model.RepuestoDto
import com.example.partsasign1.data.remote.model.SolicitudDto
import com.example.partsasign1.data.remote.model.UsuarioDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RepuestosApi {
    @GET("/api/repuestos")
    suspend fun listar(): List<RepuestoDto>

    @GET("/api/repuestos/{id}")
    suspend fun obtener(@Path("id") id: Int): RepuestoDto

    @POST("/api/repuestos")
    suspend fun crear(@Body body: RepuestoDto): RepuestoDto

    @PUT("/api/repuestos/{id}")
    suspend fun actualizar(@Path("id") id: Int, @Body body: RepuestoDto): RepuestoDto

    @DELETE("/api/repuestos/{id}")
    suspend fun eliminar(@Path("id") id: Int): Response<Unit>
}

interface OtApi {
    @GET("/api/ot-programadas")
    suspend fun listar(): List<OtProgramadaDto>

    @POST("/api/ot-programadas")
    suspend fun crear(@Body body: OtProgramadaDto): OtProgramadaDto

    @PUT("/api/ot-programadas/{id}")
    suspend fun actualizar(@Path("id") id: Int, @Body body: OtProgramadaDto): OtProgramadaDto

    @DELETE("/api/ot-programadas/{id}")
    suspend fun eliminar(@Path("id") id: Int): Response<Unit>
}

interface SolicitudesApi {
    @GET("/api/solicitudes")
    suspend fun listar(): List<SolicitudDto>

    @POST("/api/solicitudes")
    suspend fun crear(@Body body: SolicitudDto): SolicitudDto

    @PUT("/api/solicitudes/{id}")
    suspend fun actualizar(@Path("id") id: Int, @Body body: SolicitudDto): SolicitudDto

    @DELETE("/api/solicitudes/{id}")
    suspend fun eliminar(@Path("id") id: Int): Response<Unit>
}

interface AuthApi {
    @GET("/api/auth/usuarios")
    suspend fun listar(): List<UsuarioDto>

    @POST("/api/auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse
}
