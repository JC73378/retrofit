package com.example.partsasign1.Viewmodels

import androidx.lifecycle.ViewModel
import com.example.partsasign1.data.remote.repository.AuthRemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class LoginState(
    val username: String = "",
    val usernameError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val loginSuccess: Boolean = false,
    val token: String? = null,
    val rol: String? = null,
    val isLoading: Boolean = false
)

class AutentViewModel(
    private val authRepository: AuthRemoteRepository = AuthRemoteRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username = username, usernameError = null, passwordError = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    fun onLoginClick() {
        val currentState = _uiState.value
        val usernameError = if (currentState.username.isBlank()) "El usuario es obligatorio" else null
        val passError = if (currentState.password.isBlank()) "La contraseña es obligatoria" else null

        if (usernameError != null || passError != null) {
            _uiState.update {
                it.copy(usernameError = usernameError, passwordError = passError)
            }
        } else {

            _uiState.update { it.copy(isLoading = true) }

            viewModelScope.launch {

                try {
                    val result = authRepository.login(currentState.username, currentState.password)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loginSuccess = true,
                            token = result.token,
                            rol = result.rol,
                            usernameError = null,
                            passwordError = null
                        )
                    }
                } catch (e: Exception) {
                    val message = when (e) {
                        is HttpException -> "Credenciales incorrectas"
                        else -> e.message ?: "Error inesperado"
                    }
                    _uiState.update { it.copy(isLoading = false, passwordError = message) }
                }
            }
        }
    }
}
