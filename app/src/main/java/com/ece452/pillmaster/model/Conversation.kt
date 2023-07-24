package com.ece452.pillmaster.model

import java.util.*

data class Conversation(
    var id: String = Date().time.toString(),
    var title: String = "",
    var createdAt: Date = Date(),
)