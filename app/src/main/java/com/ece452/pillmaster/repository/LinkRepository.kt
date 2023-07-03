package com.ece452.pillmaster.repository

import com.ece452.pillmaster.model.LinkedUser
import com.ece452.pillmaster.model.User
import com.ece452.pillmaster.di.FirebaseModule
import com.ece452.pillmaster.repository.AuthRepository
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface ILinkRepository {
    val userid: String
    val linkedGivers: Flow<List<LinkedUser>> // Givers linked to the Receiver
    val linkedReceivers: Flow<List<LinkedUser>> // Receivers linked to the Giver

    suspend fun linkbyEmail(email: String, receiverOrGiver: String, onComplete: (Boolean)->Unit)
    // TODO: On LinkedUserScreen, show each linked user's email and a button to delete
    suspend fun deleteLinkedGiver(documentId: String) 
    suspend fun deleteLinkedReceiver(documentId: String) 
}

class LinkRepository
@Inject constructor(private val firestore: FirebaseFirestore, private val auth: AuthRepository)
: ILinkRepository {
    override val userid: String = auth.getUserId()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val linkedGivers: Flow<List<LinkedUser>>
        get() =
        auth.currentUserFlow.flatMapLatest { user ->
            firestore.collection(RECEIVER_COLLECTION).document(user.id).collection(LINK_COLLECTION).dataObjects()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val linkedReceivers: Flow<List<LinkedUser>>
        get() =
        auth.currentUserFlow.flatMapLatest { user ->
            firestore.collection(GIVER_COLLECTION).document(user.id).collection(LINK_COLLECTION).dataObjects()
        }

    override suspend fun linkbyEmail(
       email: String,
       receiverOrGiver: String, //Who is linking another, has to enter "receivers" or "givers"
       onComplete: (Boolean) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            // Perform the link operation in a background thread
            val link = firestore
                .collection(USER_COLLECTION)
                .whereEqualTo(EMAIL_FIELD, email)
                .limit(1)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onComplete.invoke(true)
                    } else {
                        onComplete.invoke(false) // Toast "Unable to find user with the provided email"
                    }
                }.await().toObject()
            val linkedUser = LinkedUser()
            linkedUser.linkedUserId = link.id
            linkedUser.linkedUserEmail = link.email
            firestore.collection(receiverOrGiver).document(userid).collection(LINK_COLLECTION).add(linkedUser).await()
        }
    }

    override suspend fun deleteLinkedGiver(documentId: String) {
        firestore.collection(RECEIVER_COLLECTION).document(userid).collection(LINK_COLLECTION).document(documentId).delete().await()
    }

    override suspend fun deleteLinkedReceiver(documentId: String) {
        firestore.collection(GIVER_COLLECTION).document(userid).collection(LINK_COLLECTION).document(documentId).delete().await()
    }

    companion object {
        private const val USER_COLLECTION = "users"
        private const val USER_ID_FIELD = "id"
        private const val EMAIL_FIELD = "email"
        private const val RECEIVER_COLLECTION = "receivers"
        private const val GIVER_COLLECTION = "givers"
        private const val LINK_COLLECTION = "links"
    }
}