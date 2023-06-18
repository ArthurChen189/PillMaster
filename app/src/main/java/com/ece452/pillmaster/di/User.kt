package com.ece452.pillmaster.model

import com.ece452.pillmaster.utils.UserRole

data class User(
    val id: String,
    val email: String,
    val name: String,
    val password: String,
    val roles: List<UserRole>
) {
    override fun toString(): String {
        val rolesString = roles.joinToString(",") { it.name }
        return "$id|$email|$name|$password|$rolesString"
    }

    companion object {
        fun fromString(userString: String): User {
            val userFields = userString.split("|")
            val id = userFields[0]
            val email = userFields[1]
            val name = userFields[2]
            val password = userFields[3]
            val rolesString = userFields[4]
            val roles = rolesString.split(",").map { UserRole.valueOf(it) }
            return User(id, email, name, password, roles)
        }
    }
}