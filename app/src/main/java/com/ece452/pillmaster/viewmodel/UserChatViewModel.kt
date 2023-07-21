package com.ece452.pillmaster.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ece452.pillmaster.model.UserChatMessage
import com.ece452.pillmaster.repository.AuthRepository
import com.ece452.pillmaster.repository.IUserChatRepository
import com.ece452.pillmaster.repository.UserChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseUserChatViewModel constructor(
    private val authRepository: AuthRepository,
    private val chatRepository: IUserChatRepository
) : ViewModel() {
    var chatUiState by mutableStateOf(ChatUiState())
        protected set

    val chatHistory: Flow<List<UserChatMessage>> = chatRepository.allChatMessages

    val currentUserId: String = authRepository.getUserId()

    fun sendMessage(receiverId: String) = viewModelScope.launch {
        try {
            chatUiState.newMessage.let {
                chatRepository.sendUserChatMessage(receiverId = receiverId, message = it)
            }
        } catch (e: Exception) {
            onErrorChange(error = e.message ?: "unknown error")
            e.printStackTrace()
        }
    }

    fun onNewMessageChange(message: String) {
        chatUiState = chatUiState.copy(newMessage = message)
    }

    fun onErrorChange(error: String) {
        chatUiState = chatUiState.copy(error = error)
    }
}

@HiltViewModel
class CareGiverUserChatViewModel @Inject constructor(
    authRepository: AuthRepository,
    chatRepository: UserChatRepository
) : BaseUserChatViewModel(authRepository, chatRepository)

@HiltViewModel
class CareReceiverUserChatViewModel @Inject constructor(
    authRepository: AuthRepository,
    chatRepository: UserChatRepository
) : BaseUserChatViewModel(authRepository, chatRepository)

data class ChatUiState(
    var newMessage: String = "",
    var receiverEmail: String = "",
    val error: String = ""
)