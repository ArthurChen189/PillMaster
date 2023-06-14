package com.ece452.pillmaster.utils

sealed class AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>()
    data class Failure(val errorMessage: String) : AuthResult<Nothing>()
}