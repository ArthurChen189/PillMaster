package com.ece452.pillmaster.model

data class User(
    var id: String = "",
    var email: String = ""
) {
    override fun toString(): String {
        return "$id|$email"
    }

    companion object {
        fun fromString(userString: String): User {
            val userFields = userString.split("|")
            val id = userFields[0]
            val email = userFields[1]
            return User(id, email)
        }
    }
}