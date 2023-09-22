package ru.yarsu.domain.operations

import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

fun hashing(password: String, salt: String): String {
    val spec = PBEKeySpec(password.toCharArray(), salt.toByteArray(), 65536, 256)
    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    val stringBuilder = StringBuilder()
    for (byte in factory.generateSecret(spec).encoded) {
        val hex = String.format("%02x", byte.toInt() and 0xFF)
        stringBuilder.append(hex)
    }
    return stringBuilder.toString()
}
