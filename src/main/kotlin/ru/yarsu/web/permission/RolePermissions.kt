package ru.yarsu.web.permission

object RolePermissions {
    val GUEST = Permission(
        "guest",
        canCreateNewAnimal = false,
        canManageUsers = false,
    )

    val USER = Permission(
        "user",
        canCreateNewAnimal = true,
        canManageUsers = false,
    )

    val ADMINISTRATOR = Permission(
        "admin",
        canCreateNewAnimal = true,
        canManageUsers = true,
    )
}
