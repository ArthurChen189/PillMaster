package com.ece452.pillmaster.model

data class User(
    var id: String = "",
    var email: String = ""
) {
    override fun toString(): String {
        return "$id|$email|$name|$password"
    }

    companion object {
        fun fromString(userString: String): User {
            val userFields = userString.split("|")
            val id = userFields[0]
            val email = userFields[1]
            val name = userFields[2]
            val password = userFields[3]
            return User(id, email, name, password)
        }
    }
}