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

/**
 * ViewModel class for the login screen.
 * Used Resources: https://www.youtube.com/watch?v=n7tUmLP6pdo
 * @param repository The instance of the AuthRepository used for authentication operations.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository, // authentication
): ViewModel() {

    val hasUser: Boolean
        get() = repository.hasUser()

    val currentUserId: String
        get() = repository.getUserId()

    /**
     * State for managing the login UI components.
     */
    var loginUiState by mutableStateOf(LoginUiState())
        private set

    /**
     * Initiates the login process for the user.
     *
     * @param context The application context used for displaying toasts.
     */
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

    /**
     * Initiates the user signup process.
     *
     * @param context The application context used for displaying toasts.
     */
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

    /**
     * Signs out the current user.
     */
    fun signoutUser() = repository.signout()

    /**
     * Updates the values in the login UI state.
     */
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

    // When user toggles the privacy policy checkbox
    fun onPolicyToggled(policyAccepted: Boolean) {
        loginUiState = loginUiState.copy(policyAccepted = policyAccepted)
    }

    /**
     * Validates the login form by checking if the email and password are not blank.
     *
     * @return True if the login form is valid, otherwise false.
     */
    private fun validateLoginForm() =
        loginUiState.userName.isNotBlank() && loginUiState.password.isNotBlank()

    /**
     * Validates the signup form by checking if the email, password, and confirm password are not blank.
     *
     * @return True if the signup form is valid, otherwise false.
     */
    private fun validateSignupForm() =
        loginUiState.userNameSignUp.isNotBlank()
                && loginUiState.passwordSignUp.isNotBlank()
                && loginUiState.confirmPasswordSignUp.isNotBlank()
}

/**
 * Data class representing the state of the login UI.
 *
 * @param userName The username value in the login form.
 * @param password The password value in the login form.
 * @param userNameSignUp The username value in the signup form.
 * @param passwordSignUp The password value in the signup form.
 * @param confirmPasswordSignUp The confirm password value in the signup form.
 * @param isLoading Indicates if a login/signup operation is in progress.
 * @param isSuccessLogin Indicates if the login/signup operation was successful.
 * @param signUpError The error message for signup failures.
 * @param loginError The error message for login failures.
 * @param policyAccepted Indicates if the privacy policy checkbox is toggled.
 */
data class LoginUiState(
    val userName: String = "",
    val password: String = "",
    val userNameSignUp: String = "",
    val passwordSignUp: String = "",
    val confirmPasswordSignUp: String = "",
    val isLoading: Boolean = false,
    val isSuccessLogin: Boolean = false,
    val signUpError: String? = null,
    val loginError: String? = null,
    val policyAccepted: Boolean = false,
)