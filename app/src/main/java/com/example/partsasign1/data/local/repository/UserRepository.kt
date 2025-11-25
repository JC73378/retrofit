package com.example.partsasign1.data.local.repository

import com.example.partsasign1.data.local.user.UserDao       // DAO de usuario
import com.example.partsasign1.data.local.user.UserEntity    // Entidad de usuario


class UserRepository(private val userDao: UserDao) {

    suspend fun login(email: String, password: String): Result<UserEntity> {
        val user = userDao.login(email, password)
        return if (user != null) {
            Result.success(user)  // ← Retorna el usuario completo si existe
        } else {
            Result.failure(Exception("Credenciales inválidas"))
        }
    }
}

