package com.ece452.pillmaster.model

import com.google.firebase.firestore.DocumentId

/**
 * Data class representing a UserChatMessage entity.
 *
 * @param id: The unique identifier of the chat message.
 * @param senderId: The unique identifier of the user who sent the message.
 * @param receiverId: The unique identifier of the user who received the message.
 * @param message: The content of the chat message.
 * @param timestamp: The timestamp when the message was sent (formatted as a String).
 */
data class UserChatMessage (
    @DocumentId var id: String = "", // id of chat message
    var senderId: String = "",
    var receiverId: String = "",
    var message: String = "",
    var timestamp: String = ""
)