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
private const val USER_CHAT_MESSAGE_COLLECTION = "user_chat"

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
     * @throws IllegalArgumentException If the message is empty or blank.I/System.out: COMMON HEADERS
    I/System.out: -> alt-svc: h3=":443"; ma=86400
    I/System.out: -> cf-cache-status: DYNAMIC
    I/System.out: -> cf-ray: 7ece9fa3ae0e36d0-YYZ
    I/System.out: -> content-length: 262
    I/System.out: -> content-type: application/json; charset=utf-8
    I/System.out: -> date: Wed, 26 Jul 2023 18:14:40 GMT
    I/System.out: -> server: cloudflare
    I/System.out: -> strict-transport-security: max-age=15724800; includeSubDomains
    I/System.out: -> vary: Origin
    I/System.out: -> x-request-id: 242bac253544dc6787c91ef77629a360

    --------- beginning of crash
    E/AndroidRuntime: FATAL EXCEPTION: main
    Process: com.ece452.pillmaster, PID: 14609
    com.aallam.openai.api.exception.AuthenticationException: Incorrect API key provided: openai-token. You can find your API key at https://platform.openai.com/account/api-keys.
    at com.aallam.openai.client.internal.http.HttpTransport.openAIAPIException(HttpTransport.kt:66)
    at com.aallam.openai.client.internal.http.HttpTransport.handleException(HttpTransport.kt:48)
    at com.aallam.openai.client.internal.http.HttpTransport.perform(HttpTransport.kt:23)
    at com.aallam.openai.client.internal.http.HttpTransport$perform$1.invokeSuspend(Unknown Source:15)
    at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
    at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:104)
    at androidx.compose.ui.platform.AndroidUiDispatcher.performTrampolineDispatch(AndroidUiDispatcher.android.kt:81)
    at androidx.compose.ui.platform.AndroidUiDispatcher.access$performTrampolineDispatch(AndroidUiDispatcher.android.kt:41)
    at androidx.compose.ui.platform.AndroidUiDispatcher$dispatchCallback$1.run(AndroidUiDispatcher.android.kt:57)
    at android.os.Handler.handleCallback(Handler.java:942)
    at android.os.Handler.dispatchMessage(Handler.java:99)
    at android.os.Looper.loopOnce(Looper.java:201)
    at android.os.Looper.loop(Looper.java:288)
    at android.app.ActivityThread.main(ActivityThread.java:7872)
    at java.lang.reflect.Method.invoke(Native Method)
    at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:548)
    at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:936)
    Suppressed: kotlinx.coroutines.internal.DiagnosticCoroutineContextException: [androidx.compose.ui.platform.MotionDurationScaleImpl@e037e71, androidx.compose.runtime.BroadcastFrameClock@4697456, StandaloneCoroutine{Cancelling}@2dcecd7, AndroidUiDispatcher@d8489c4]
    Caused by: io.ktor.client.plugins.ClientRequestException: Client request(POST https://api.openai.com/v1/completions) invalid: 401 . Text: "{
    "error": {
    "message": "Incorrect API key provided: openai-token. You can find your API key at https://platform.openai.com/account/api-keys.",
    "type": "invalid_request_error",
    "param": null,
    "code": "invalid_api_key"
    }
    }
    "
    at io.ktor.client.plugins.DefaultResponseValidationKt$addDefaultResponseValidation$1$1.invokeSuspend(DefaultResponseValidation.kt:54)
    at io.ktor.client.plugins.DefaultResponseValidationKt$addDefaultResponseValidation$1$1.invoke(Unknown Source:8)
    at io.ktor.client.plugins.DefaultResponseValidationKt$addDefaultResponseValidation$1$1.invoke(Unknown Source:4)
    at io.ktor.client.plugins.HttpCallValidator.validateResponse(HttpCallValidator.kt:51)
    at io.ktor.client.plugins.HttpCallValidator.access$validateResponse(HttpCallValidator.kt:43)
    at io.ktor.client.plugins.HttpCallValidator$Companion$install$3.invokeSuspend(HttpCallValidator.kt:152)
    at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
    at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:106)
    ... 11 more
    I/Process: Sending signal. PID: 14609 SIG: 9

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