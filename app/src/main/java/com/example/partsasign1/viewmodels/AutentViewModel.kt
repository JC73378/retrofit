package com.example.partsasign1.Viewmodels

import androidx.lifecycle.ViewModel
import com.example.partsasign1.domain.Validation.validateEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.partsasign1.data.local.repository.UserRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class LoginState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val loginSuccess: Boolean = false,
    val currentUser: Any? = null,
    val isLoading: Boolean = false
)


class AutentViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    fun onLoginClick() {
        val currentState = _uiState.value
        val emailError = validateEmail(currentState.email)
        val passError = if (currentState.password.isBlank()) "La contraseña es obligatoria" else null

        if (emailError != null || passError != null) {
            _uiState.update {
                it.copy(emailError = emailError, passwordError = passError)
            }
        } else {

            _uiState.update { it.copy(isLoading = true) }

            viewModelScope.launch {

                val result = userRepository.login(currentState.email, currentState.password)
                _uiState.update { it.copy(isLoading = false) }

                if (result.isSuccess) {
                    _uiState.update { it.copy(loginSuccess = true) }
                } else {
                    _uiState.update { it.copy(passwordError = "Credenciales incorrectas") }
                }
            }
        }
    }
}