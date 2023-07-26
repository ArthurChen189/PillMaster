package com.ece452.pillmaster.repository
import android.os.Build
import androidx.annotation.RequiresApi
import com.ece452.pillmaster.model.Contact
import com.ece452.pillmaster.model.UserChatMessage
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.dataObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.tasks.await
import java.nio.file.Paths.get
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private const val SENDER_ID_FIELD = "senderId"
private const val RECEIVER_ID_FIELD = "receiverId"
private const val TIMESTAMP_FIELD = "timestamp"
private const val USER_CHAT_MESSAGE_COLLECTION = "chat"

/**
 * Interface for the User Chat Repository.
 * Provides methods to manage user chat messages.
 */
interface IUserChatRepository {
    val allChatMessages: Flow<List<UserChatMessage>>
    /**
     * Sends a user chat message to the specified receiver and updates the chat messages flow.
     *
     * @param receiverId The unique identifier of the message receiver.
     * @param message The content of the chat message to be sent.
     * @return The UserChatMessage object representing the sent message.
     * @throws IllegalArgumentException If the message is empty or blank.
     * @throws IllegalStateException If the current user is not found.
     */
    suspend fun sendUserChatMessage(receiverId: String, message: String): UserChatMessage
}

/**
 * Repository class for managing user chat messages.
 * Implements the IUserChatRepository interface.
 */
class UserChatRepository
@Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AuthRepository)
    : IUserChatRepository {
    private val sentMessages: Filter = Filter.equalTo(SENDER_ID_FIELD, auth.getUserId())
    private val receivedMessage: Filter = Filter.equalTo(RECEIVER_ID_FIELD, auth.getUserId())
    private val compositeFilter = Filter.or(sentMessages, receivedMessage)

    private val _chatMessagesFlow = MutableStateFlow<List<UserChatMessage>>(emptyList())
    val chatMessagesFlow: StateFlow<List<UserChatMessage>> = _chatMessagesFlow

    override val allChatMessages: Flow<List<UserChatMessage>>
        get() = chatMessagesFlow

    /**
     * Fetches chat messages from Firestore for the current user and updates the chat messages flow.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun fetchChatMessages(): Flow<List<UserChatMessage>> =
        auth.currentUserFlow.flatMapLatest { user ->
            firestore.collection(USER_CHAT_MESSAGE_COLLECTION)
                .where(compositeFilter)
                .orderBy(TIMESTAMP_FIELD, Query.Direction.ASCENDING)
                .dataObjects()
        }

    /**
     * Updates the chat messages flow with the latest chat messages.
     */
    suspend fun updateChatMessages() {
        val user = auth.currentUserFlow.firstOrNull()
        if (user != null) {
            fetchChatMessages().collectLatest { chatMessages ->
                _chatMessagesFlow.value = chatMessages
            }
        }
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
        updateChatMessages()
        return chatMessage
    }
}