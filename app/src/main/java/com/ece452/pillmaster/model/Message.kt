package com.ece452.pillmaster.model

import java.util.*

data class Message (
    var id: String = Date().time.toString(),
    var conversationId: String = "",
    var question: String = "",
    var answer: String = "",
    var createdAt: Date = Date(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Message

        if (id != other.id) return false
        if (conversationId != other.conversationId) return false
        if (question != other.question) return false
        if (answer != other.answer) return false
        if (createdAt != other.createdAt) return false

        return true
    }
}