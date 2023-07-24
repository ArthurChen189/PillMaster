package com.ece452.pillmaster.model

import com.google.firebase.firestore.DocumentId

data class UserChatMessage (
    @DocumentId var id: String = "", // id of chat message
    var senderId: String = "",
    var receiverId: String = "",
    var message: String = "",
    var timestamp: String = ""
)