package com.ece452.pillmaster.repository
import android.os.Build
import androidx.annotation.RequiresApi
import com.ece452.pillmaster.model.UserChatMessage
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.dataObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private const val SENDER_ID_FIELD = "senderId"
private const val RECEIVER_ID_FIELD = "receiverId"
private const val TIMESTAMP_FIELD = "timestamp"
private const val USER_CHAT_MESSAGE_COLLECTION = "chat"

interface IUserChatRepository {
    val allChatMessages: Flow<List<UserChatMessage>>
    suspend fun sendUserChatMessage(receiverId: String, message: String): UserChatMessage
}

@OptIn(ExperimentalCoroutinesApi::class)
class UserChatRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AuthRepository,
) : IUserChatRepository {
    private val sentMessages: Filter = Filter.equalTo(SENDER_ID_FIELD, auth.getUserId())
    private val receivedMessage: Filter = Filter.equalTo(RECEIVER_ID_FIELD, auth.getUserId())
    private val compositeFilter = Filter.or(sentMessages, receivedMessage)

    override val allChatMessages: Flow<List<UserChatMessage>>  =
        auth.currentUserFlow.flatMapLatest { user ->
            firestore.collection(USER_CHAT_MESSAGE_COLLECTION)
                .where(compositeFilter)
                .orderBy(TIMESTAMP_FIELD, Query.Direction.ASCENDING)
                .dataObjects()
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun sendUserChatMessage(receiverId: String, message: String): UserChatMessage {
        if (message.isBlank()) {
            throw IllegalArgumentException("Message cannot be empty.")
        }

        val currentUser = auth.currentUserFlow.firstOrNull()
            ?: throw IllegalStateException("Current user not found.")

        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedTime = currentTime.format(formatter)
        val chatMessage = UserChatMessage(
            senderId = currentUser.userId,
            receiverId = receiverId,
            message = message,
            timestamp = formattedTime
        )

        chatMessage.id = firestore.collection(USER_CHAT_MESSAGE_COLLECTION).add(chatMessage).await().id
        return chatMessage
    }
}