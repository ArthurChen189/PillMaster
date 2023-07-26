package com.ece452.pillmaster.repository

import com.ece452.pillmaster.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

// Interface of the Authentication repository
interface IAuthRepository {
    val currentUser: FirebaseUser?
    val currentUserFlow: Flow<User>

    /**
     * Checks if there is a currently authenticated user.
     *
     * @return True if there is a currently authenticated user, otherwise false.
     */
    fun hasUser(): Boolean

    /**
     * Returns the unique identifier (UID) of the currently authenticated user.
     *
     * @return The UID of the currently authenticated user, or an empty string if not authenticated.
     */
    fun getUserId(): String

    /**
     * Retrieves the User profile from Firestore by user ID.
     *
     * @param userId The unique identifier of the user whose profile is to be fetched.
     * @return The User model representing the user's profile.
     * @throws Exception If the user profile is not found in Firestore or if it is null.
     */
    suspend fun getUserProfileById(userId: String): User

    /**
     * Retrieves the User profile from Firestore by email.
     *
     * @param email The email of the user whose profile is to be fetched.
     * @return The User model representing the user's profile.
     * @throws Exception If the user profile is not found in Firestore or if it is null.
     */
    suspend fun getUserProfileByEmail(email: String): User

    /**
     * Performs the login operation with email and password.
     *
     * @param email The user's email for login.
     * @param password The user's password for login.
     * @param onComplete Callback function invoked after login completion.
     */
    suspend fun login(
        email: String,
        password: String,
        onComplete: (Boolean)->Unit
    )

    /**
     * Performs the signup operation with email and password.
     *
     * @param email The user's email for signup.
     * @param password The user's password for signup.
     * @param onComplete Callback function invoked after signup completion.
     */
    suspend fun signup(
       email: String,
       password: String,
       onComplete: (Boolean)->Unit
    )

    /**
     * Signs out the current user.
     */
    fun signout()
}

/**
 * Implementation of the Authentication repository.
 * This class provides functionalities for user authentication and user profile retrieval.
 * Used Resources: https://www.youtube.com/watch?v=n7tUmLP6pdo
 */
class AuthRepository
@Inject constructor(
    private val firestore: FirebaseFirestore, // database
    private val auth: FirebaseAuth, // authentication
    ) : IAuthRepository{

    override val currentUser: FirebaseUser? = auth.currentUser

    override val currentUserFlow: Flow<User>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { User(userId = it.uid) } ?: User())
                }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    override fun hasUser(): Boolean = auth.currentUser != null

    override fun getUserId(): String = auth.currentUser?.uid.orEmpty()

    override suspend fun getUserProfileById(userId: String): User {
        val querySnapshot = firestore.collection(USER_COLLECTION)
            .whereEqualTo(USER_ID_FIELD, userId)
            .get()
            .await()

        val userDocument = querySnapshot.documents.firstOrNull()
        if (userDocument != null) {
            return userDocument.toObject(User::class.java) ?: throw Exception("User profile is null")
        } else {
            throw Exception("User profile not found")
        }
    }

    // Get User data model from firestore by email
    override suspend fun getUserProfileByEmail(email: String): User {
        val querySnapshot = firestore.collection(USER_COLLECTION)
            .whereEqualTo(EMAIL_FIELD, email)
            .get()
            .await()

        val userDocument = querySnapshot.documents.firstOrNull()
        if (userDocument != null) {
            return userDocument.toObject(User::class.java) ?: throw Exception("User profile is null")
        } else {
            throw Exception("User profile not found")
        }
    }

    override suspend fun login(
        email: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            // Perform the login operation in a background thread
            auth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onComplete.invoke(true)
                    } else {
                        onComplete.invoke(false)
                    }
                }.await()
        }
    }

    override suspend fun signup(
       email: String,
       password: String,
       onComplete: (Boolean) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            // Perform the signup operation in a background thread
            auth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Add user to firestore database when sign up
                        val user = User(
                            userId = task.result?.user!!.uid,
                            email = email
                        )
                        firestore.collection(USER_COLLECTION).add(user)
                        onComplete.invoke(true)
                    } else {
                        onComplete.invoke(false)
                    }
                }.await()
        }
    }

    override fun signout() {
        auth.signOut()
    }

    companion object {
        private const val USER_COLLECTION = "users"
        private const val USER_ID_FIELD = "userId"
        private const val EMAIL_FIELD = "email"
    }
}