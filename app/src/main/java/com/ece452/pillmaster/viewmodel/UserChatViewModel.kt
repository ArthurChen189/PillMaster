package com.ece452.pillmaster.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ece452.pillmaster.model.UserChatMessage
import com.ece452.pillmaster.repository.AuthRepository
import com.ece452.pillmaster.repository.UserChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Abstract ViewModel class representing the base functionality for managing user chat.
 * @param authRepository The instance of the AuthRepository used for authentication operations.
 * @param chatRepository The instance of the UserChatRepository for user chat operations.
 */
abstract class BaseUserChatViewModel constructor(
    private val authRepository: AuthRepository,
    private val chatRepository: UserChatRepository
) : ViewModel() {
    var chatUiState by mutableStateOf(ChatUiState())
        protected set

    val chatHistory: Flow<List<UserChatMessage>> = chatRepository.allChatMessages

    val currentUserId: String = authRepository.getUserId()

    /**
     * Initializes the ViewModel by updating the chat messages.
     */
    init {
        viewModelScope.launch {
            chatRepository.updateChatMessages()
        }
    }

    /**
     * Sends a new message to the specified receiver.
     * @param receiverId The ID of the message receiver.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(receiverId: String) = viewModelScope.launch {
        try {
            chatUiState.newMessage.let {
                chatRepository.sendUserChatMessage(receiverId = receiverId, message = it)
                onNewMessageChange("")
            }
        } catch (e: Exception) {
            onErrorChange(error = e.message ?: "unknown error")
            e.printStackTrace()
        }
    }

    /**
     * Updates the values in the chat UI state.
     * @param message The new message to be sent.
     */
    fun onNewMessageChange(message: String) {
        chatUiState = chatUiState.copy(newMessage = message)
    }

    fun onErrorChange(error: String) {
        chatUiState = chatUiState.copy(error = error)
    }
}

/**
 * ViewModel class for managing caregiver user chat.
 * @param authRepository The instance of the AuthRepository used for authentication operations.
 * @param chatRepository The instance of the UserChatRepository for caregiver user chat operations.
 */
@HiltViewModel
class CareGiverUserChatViewModel @Inject constructor(
    authRepository: AuthRepository,
    chatRepository: UserChatRepository
) : BaseUserChatViewModel(authRepository, chatRepository)

/**
 * ViewModel class for managing care receiver user chat.
 * @param authRepository The instance of the AuthRepository used for authentication operations.
 * @param chatRepository The instance of the UserChatRepository for care receiver user chat operations.
 */
@HiltViewModel
class CareReceiverUserChatViewModel @Inject constructor(
    authRepository: AuthRepository,
    chatRepository: UserChatRepository
) : BaseUserChatViewModel(authRepository, chatRepository)

/**
 * Data class representing the state of the user chat-related UI elements.
 * @param newMessage The new chat message to be sent.
 * @param receiverEmail The email of the message receiver.
 * @param error The error message to be displayed.
 */
data class ChatUiState(
    var newMessage: String = "",
    var receiverEmail: String = "",
    val error: String = ""
)