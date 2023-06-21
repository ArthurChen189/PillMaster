package com.ece452.pillmaster.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ece452.pillmaster.repository.AuthRepository
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// Used Resources: https://www.youtube.com/watch?v=n7tUmLP6pdo
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
    val currentUser = repository.currentUser

    val hasUser: Boolean
        get() = repository.hasUser()

    var loginUiState by mutableStateOf(LoginUiState())
        private set

    fun loginUser(context: Context) = viewModelScope.launch {
        try {
            if (!validateLoginForm()) {
                throw IllegalArgumentException("email and password can not be empty")
            }

            loginUiState = loginUiState.copy(isLoading = true)
            loginUiState = loginUiState.copy(loginError = null)

            repository.login(
                loginUiState.userName,
                loginUiState.password
            ) { isSuccessful ->
                if (isSuccessful) {
                    Toast.makeText(
                        context,
                        "success login",
                        Toast.LENGTH_SHORT
                    ).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = true)
                } else {
                    Toast.makeText(
                        context,
                        "failed login",
                        Toast.LENGTH_SHORT
                    ).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = false)
                }
            }
        } catch (e: Exception) {
            loginUiState = loginUiState.copy(loginError = e.localizedMessage)
            e.printStackTrace()
        } finally {
            loginUiState = loginUiState.copy(isLoading = false)
        }
    }

    fun createUser(context: Context) = viewModelScope.launch {
        try {
            if (!validateSignupForm()) {
                throw IllegalArgumentException("email and password can not be empty")
            }
            if (loginUiState.passwordSignUp != loginUiState.confirmPasswordSignUp) {
                throw IllegalArgumentException("passwords do not match")
            }

            loginUiState = loginUiState.copy(isLoading = true)
            loginUiState = loginUiState.copy(signUpError = null)

            repository.signup(
                loginUiState.userNameSignUp,
                loginUiState.passwordSignUp
            ) { isSuccessful ->
                if (isSuccessful) {
                    Toast.makeText(
                        context,
                        "success signup",
                        Toast.LENGTH_SHORT
                    ).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = true)
                } else {
                    Toast.makeText(
                        context,
                        "failed signup",
                        Toast.LENGTH_SHORT
                    ).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = false)
                }
            }
        } catch (e: Exception) {
            loginUiState = loginUiState.copy(signUpError = e.localizedMessage)
            e.printStackTrace()
        } finally {
            loginUiState = loginUiState.copy(isLoading = false)
        }
    }

    fun signoutUser() = repository.signout()

    fun onUserNameChange(userName: String) {
        loginUiState = loginUiState.copy(userName = userName)
    }

    fun onPasswordChange(password: String) {
        loginUiState = loginUiState.copy(password = password)
    }

    fun onUserNameSignUpChange(userNameSignUp: String) {
        loginUiState = loginUiState.copy(userNameSignUp = userNameSignUp)
    }

    fun onPasswordSignUpChange(passwordSignUp: String) {
        loginUiState = loginUiState.copy(passwordSignUp = passwordSignUp)
    }

    fun onConfirmPasswordSignUpChange(confirmPasswordSignUp: String) {
        loginUiState = loginUiState.copy(confirmPasswordSignUp = confirmPasswordSignUp)
    }

    private fun validateLoginForm() =
        loginUiState.userName.isNotBlank() && loginUiState.password.isNotBlank()

    private fun validateSignupForm() =
        loginUiState.userNameSignUp.isNotBlank()
                && loginUiState.passwordSignUp.isNotBlank()
                && loginUiState.confirmPasswordSignUp.isNotBlank()
}

data class LoginUiState(
    val userName: String = "",
    val password: String = "",
    val userNameSignUp: String = "",
    val passwordSignUp: String = "",
    val confirmPasswordSignUp: String = "",
    val isLoading: Boolean = false,
    val isSuccessLogin: Boolean = false,
    val signUpError: String? = null,
    val loginError: String? = null
)