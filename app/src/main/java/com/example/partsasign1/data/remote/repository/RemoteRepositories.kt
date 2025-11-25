package com.example.partsasign1.data.remote.repository

import com.example.partsasign1.data.remote.RetrofitClients
import com.example.partsasign1.data.remote.api.AuthApi
import com.example.partsasign1.data.remote.api.OtApi
import com.example.partsasign1.data.remote.api.RecepcionApi
import com.example.partsasign1.data.remote.api.RepuestosApi
import com.example.partsasign1.data.remote.model.LoginRequest
import com.example.partsasign1.data.remote.model.LoginResponse
import com.example.partsasign1.data.remote.model.OtProgramadaDto
import com.example.partsasign1.data.remote.model.RecepcionDto
import com.example.partsasign1.data.remote.model.RepuestoDto
import com.example.partsasign1.data.remote.model.UsuarioDto
import retrofit2.Response

class RepuestosRemoteRepository(
    private val api: RepuestosApi = RetrofitClients.repuestosApi
) {
    suspend fun listar(): List<RepuestoDto> = api.listar()
    suspend fun crear(body: RepuestoDto): RepuestoDto = api.crear(body)
    suspend fun actualizar(id: Int, body: RepuestoDto): RepuestoDto = api.actualizar(id, body)
    suspend fun eliminar(id: Int): Response<Unit> = api.eliminar(id)
}

class OtRemoteRepository(
    private val api: OtApi = RetrofitClients.otApi
) {
    suspend fun listar(): List<OtProgramadaDto> = api.listar()
    suspend fun crear(body: OtProgramadaDto): OtProgramadaDto = api.crear(body)
    suspend fun actualizar(id: Int, body: OtProgramadaDto): OtProgramadaDto = api.actualizar(id, body)
    suspend fun eliminar(id: Int): Response<Unit> = api.eliminar(id)
}

class RecepcionRemoteRepository(
    private val api: RecepcionApi = RetrofitClients.recepcionApi
) {
    suspend fun listar(): List<RecepcionDto> = api.listar()
    suspend fun crear(body: RecepcionDto): RecepcionDto = api.crear(body)
    suspend fun actualizar(id: Int, body: RecepcionDto): RecepcionDto = api.actualizar(id, body)
    suspend fun eliminar(id: Int): Response<Unit> = api.eliminar(id)
}

class AuthRemoteRepository(
    private val api: AuthApi = RetrofitClients.authApi
) {
    suspend fun login(username: String, password: String): LoginResponse =
        api.login(LoginRequest(username = username, password = password))

    suspend fun listarUsuarios(): List<UsuarioDto> = api.listar()
}
