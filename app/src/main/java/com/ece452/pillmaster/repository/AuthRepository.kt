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

interface IAuthRepository {
    val currentUser: FirebaseUser?
    val currentUserFlow: Flow<User>
    fun hasUser(): Boolean
    fun getUserId(): String
    suspend fun getUserProfileById(userId: String): User
    suspend fun getUserProfileByEmail(email: String): User
    suspend fun login(
        email: String,
        password: String,
        onComplete: (Boolean)->Unit
    )
    suspend fun signup(
       email: String,
       password: String,
       onComplete: (Boolean)->Unit
    )
    fun signout()
}

// Used Resources: https://www.youtube.com/watch?v=n7tUmLP6pdo
class AuthRepository
@Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
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