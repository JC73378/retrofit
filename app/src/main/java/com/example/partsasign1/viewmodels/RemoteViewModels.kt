package com.example.partsasign1.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.partsasign1.data.remote.model.OtProgramadaDto
import com.example.partsasign1.data.remote.model.RepuestoDto
import com.example.partsasign1.data.remote.model.LoginResponse
import com.example.partsasign1.data.remote.model.UsuarioDto
import com.example.partsasign1.data.remote.repository.AuthRemoteRepository
import com.example.partsasign1.data.remote.repository.OtRemoteRepository
import com.example.partsasign1.data.remote.repository.RepuestosRemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RepuestosRemoteViewModel(
    private val repo: RepuestosRemoteRepository = RepuestosRemoteRepository()
) : ViewModel() {
    private val _items = MutableStateFlow<List<RepuestoDto>>(emptyList())
    val items: StateFlow<List<RepuestoDto>> = _items

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        refrescar()
    }

    fun refrescar() = viewModelScope.launch {
        _loading.value = true
        _error.value = null
        try {
            _items.value = repo.listar()
        } catch (e: Exception) {
            _error.value = e.message
        } finally {
            _loading.value = false
        }
    }

    fun crear(dto: RepuestoDto) = viewModelScope.launch {
        _loading.value = true
        _error.value = null
        try {
            repo.crear(dto)
            refrescar()
        } catch (e: Exception) {
            _error.value = e.message
        } finally {
            _loading.value = false
        }
    }

    fun actualizar(id: Int, dto: RepuestoDto) = viewModelScope.launch {
        _loading.value = true
        _error.value = null
        try {
            repo.actualizar(id, dto)
            refrescar()
        } catch (e: Exception) {
            _error.value = e.message
        } finally {
            _loading.value = false
        }
    }

    fun eliminar(id: Int) = viewModelScope.launch {
        _loading.value = true
        _error.value = null
        try {
            repo.eliminar(id)
            refrescar()
        } catch (e: Exception) {
            _error.value = e.message
        } finally {
            _loading.value = false
        }
    }
}

class OtRemoteViewModel(
    private val repo: OtRemoteRepository = OtRemoteRepository()
) : ViewModel() {
    private val _items = MutableStateFlow<List<OtProgramadaDto>>(emptyList())
    val items: StateFlow<List<OtProgramadaDto>> = _items

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        refrescar()
    }

    fun refrescar() = viewModelScope.launch {
        _loading.value = true
        _error.value = null
        try {
            _items.value = repo.listar()
        } catch (e: Exception) {
            _error.value = e.message
        } finally {
            _loading.value = false
        }
    }

    fun crear(dto: OtProgramadaDto) = viewModelScope.launch {
        _loading.value = true
        _error.value = null
        try {
            repo.crear(dto)
            refrescar()
        } catch (e: Exception) {
            _error.value = e.message
        } finally {
            _loading.value = false
        }
    }

    fun actualizar(id: Int, dto: OtProgramadaDto) = viewModelScope.launch {
        _loading.value = true
        _error.value = null
        try {
            repo.actualizar(id, dto)
            refrescar()
        } catch (e: Exception) {
            _error.value = e.message
        } finally {
            _loading.value = false
        }
    }

    fun eliminar(id: Int) = viewModelScope.launch {
        _loading.value = true
        _error.value = null
        try {
            repo.eliminar(id)
            refrescar()
        } catch (e: Exception) {
            _error.value = e.message
        } finally {
            _loading.value = false
        }
    }
}

class AuthRemoteViewModel(
    private val repo: AuthRemoteRepository = AuthRemoteRepository()
) : ViewModel() {
    private val _login = MutableStateFlow<LoginResponse?>(null)
    val login: StateFlow<LoginResponse?> = _login

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun login(username: String, password: String) = viewModelScope.launch {
        _loading.value = true
        _error.value = null
        try {
            _login.value = repo.login(username, password)
        } catch (e: Exception) {
            _error.value = e.message
            _login.value = null
        } finally {
            _loading.value = false
        }
    }
}
