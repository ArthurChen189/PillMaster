package com.ece452.pillmaster.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.management.ManagementException
import com.auth0.android.management.UsersAPIClient
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.ece452.pillmaster.R
import com.ece452.pillmaster.utils.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Resources Used: https://auth0.com/blog/android-authentication-jetpack-compose-part-2/
 */
@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel() {
    private lateinit var account: Auth0
    private var cachedCredentials: Credentials? = null
    private var cachedUserProfile: UserProfile? = null

    private val LOGGER = "LoginViewModel"

    fun setAccount(context: Context) {
        account = Auth0(
            context.getString(R.string.com_auth0_client_id),
            context.getString(R.string.com_auth0_domain)
        )
    }

    fun login(context: Context) : AuthResult<Credentials> {
        if (cachedCredentials != null) {
            // User is already logged in, return success
            return AuthResult.Success(cachedCredentials!!)
        }

        val client = WebAuthProvider
            .login(account)
            .withScheme(context.getString(R.string.com_auth0_scheme))
            .withScope(context.getString(R.string.login_scopes))
            .withAudience(
                context.getString(
                    R.string.login_audience,
                    context.getString(R.string.com_auth0_domain)
                )
            )

        return runCatching {
            client.start(context, object : Callback<Credentials, AuthenticationException> {

                override fun onFailure(error: AuthenticationException) {
                    Log.e(LOGGER, "Error occurred in login(): $error")
                    throw error
                }

                override fun onSuccess(result: Credentials) {
                    Log.d(LOGGER, "login() succeeded: $result")
                    cachedCredentials = result
                }
            })
        }.fold(
            onSuccess = { AuthResult.Success(cachedCredentials!!) },
            onFailure = { AuthResult.Failure(it.message ?: "Unknown error occurred") }
        )
    }

    fun logout(context: Context): AuthResult<Unit> {
        if (cachedCredentials == null) {
            // User is not logged in, return success
            return AuthResult.Success(Unit)
        }

        val client = WebAuthProvider
            .logout(account)
            .withScheme(context.getString(R.string.com_auth0_scheme))


        return runCatching {
            client.start(context, object : Callback<Void?, AuthenticationException> {

                    override fun onFailure(error: AuthenticationException) {
                        Log.e(LOGGER, "Error occurred in logout(): $error")
                        throw error
                    }

                    override fun onSuccess(result: Void?) {
                        Log.d(LOGGER, "logout() succeeded.")
                        cachedCredentials = null
                        cachedUserProfile = null
                    }
                })
        }.fold(
            onSuccess = { AuthResult.Success(Unit) },
            onFailure = { AuthResult.Failure(it.message ?: "Unknown error occurred") }
        )
    }

    fun showUserProfile(): AuthResult<UserProfile> {
        if (cachedCredentials == null) {
            Log.e(LOGGER, "Error occurred in showUserProfile(): User not logged in.")
            return AuthResult.Failure("User not logged in.")
        }

        val client = AuthenticationAPIClient(account).userInfo(cachedCredentials!!.accessToken)

        return runCatching {
            client.start(object : Callback<UserProfile, AuthenticationException> {

                    override fun onFailure(error: AuthenticationException) {
                        Log.e(LOGGER, "Error occurred in showUserProfile(): $error")
                        throw error
                    }

                    override fun onSuccess(result: UserProfile) {
                        Log.d(LOGGER, "showUserProfile() succeeded: $result")
                        cachedUserProfile = result
                    }
                })
        }.fold(
            onSuccess = { AuthResult.Success(cachedUserProfile!!) },
            onFailure = { AuthResult.Failure(it.message ?: "Unknown error occurred") }
        )
    }

    fun getUserMetadata(): AuthResult<Map<String, Any>> {
        if (cachedCredentials == null) {
            Log.e(LOGGER, "Error occurred in getUserMetadata(): User not logged in.")
            return AuthResult.Failure("User not logged in.")
        }

        val client = UsersAPIClient(account, cachedCredentials!!.accessToken).getProfile(cachedUserProfile!!.getId()!!)

        return runCatching {
            client.start(object : Callback<UserProfile, ManagementException> {

                    override fun onFailure(error: ManagementException) {
                        Log.e(LOGGER, "Error occurred in getUserMetadata(): $error")
                        throw error
                    }

                    override fun onSuccess(result: UserProfile) {
                        Log.d(LOGGER, "getUserMetadata() succeeded: $result")
                        cachedUserProfile = result
                    }
                })
        }.fold(
            onSuccess = { AuthResult.Success(cachedUserProfile!!.getUserMetadata()) },
            onFailure = { AuthResult.Failure(it.message ?: "Unknown error occurred") }
        )
    }

    fun setUserMetadata(userMetadata: Map<String, Any>): AuthResult<UserProfile> {
        if (cachedCredentials == null) {
            return AuthResult.Failure("Invalid User.")
        }

        val client = UsersAPIClient(account, cachedCredentials!!.accessToken).updateMetadata(cachedUserProfile!!.getId()!!, userMetadata)

        return runCatching {
            client.start(object : Callback<UserProfile, ManagementException> {

                    override fun onFailure(error: ManagementException) {
                        Log.e(LOGGER, "Error occurred in setUserMetadata(): $error")
                        throw error
                    }

                    override fun onSuccess(result: UserProfile) {
                        Log.d(LOGGER, "setUserMetadata() succeeded: $result")
                        cachedUserProfile = result
                    }
                })
        }.fold(
            onSuccess = { AuthResult.Success(cachedUserProfile!!) },
            onFailure = { AuthResult.Failure(it.message ?: "Unknown error occurred") }
        )
    }
}