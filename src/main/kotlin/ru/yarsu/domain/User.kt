package ru.yarsu.domain

import ru.yarsu.web.permission.Permission
import java.time.LocalDateTime

data class User(
    val login: String,
    val password: String,
    val dateRegistration: LocalDateTime,
    val rolePermissions: Permission,
)
