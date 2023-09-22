package ru.yarsu.web.permission

data class Permission(
    val name: String,
    val canCreateNewAnimal: Boolean,
    val canManageUsers: Boolean,
)
