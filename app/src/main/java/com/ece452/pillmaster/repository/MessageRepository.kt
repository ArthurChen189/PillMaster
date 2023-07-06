package com.ece452.pillmaster.repository

import com.ece452.pillmaster.model.ChatMessage
import com.ece452.pillmaster.model.Reminder
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface MessageObserver {
    fun receiveMessage(message: ChatMessage)
}

interface IMessageRepository {
    val reminders: Flow<List<Reminder>>
    suspend fun getReminder(reminderId: String): Reminder?
    suspend fun save(reminder: Reminder): String
    suspend fun update(reminder: Reminder)
    suspend fun delete(reminderId: String)
}

class MessageRepository
@Inject constructor(private val firestore: FirebaseFirestore, private val auth: AuthRepository)