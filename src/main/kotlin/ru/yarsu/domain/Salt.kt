package ru.yarsu.domain

import java.security.SecureRandom
import java.util.Base64

const val LEN = 100

object Salt {
    private val salts = mutableMapOf<String, String>()

    fun generateSalt(login: String): String {
        val randomBytes = ByteArray(LEN)
        val random = SecureRandom()
        random.nextBytes(randomBytes)
        val salt = Base64.getEncoder().encodeToString(randomBytes)
        salts[login] = salt
        return salt
    }

    fun getSalt(login: String): String? {
        return salts[login]
    }
}
