package com.example.partsasign1.data.local.repository

import com.example.partsasign1.data.local.user.UserEntity

/**
 * Persistencia local removida: siempre delegar login al backend.
 */
class UserRepository {
    suspend fun login(email: String, password: String): Result<UserEntity> =
        Result.failure(UnsupportedOperationException("Login local deshabilitado; usa API remota."))
}
