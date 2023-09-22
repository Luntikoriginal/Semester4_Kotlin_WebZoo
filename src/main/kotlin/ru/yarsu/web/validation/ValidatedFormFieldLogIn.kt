package ru.yarsu.web.validation

import ru.yarsu.domain.ListOfUsers
import ru.yarsu.domain.Salt
import ru.yarsu.domain.operations.hashing

data class ValidatedFormFieldLogIn(
    val login: String?,
    val password: String?,
    val users: ListOfUsers,
) {
    fun validateForm(): FormErrors {
        val pattern = Regex("[a-zA-Z_0-9-!]+")
        val errors = mutableMapOf<String, MutableList<String>>()
        if (login.isNullOrEmpty()) {
            errors.getOrPut("login") { mutableListOf() }.add("Не удалось получить Логин")
        }
        if (password.isNullOrEmpty()) {
            errors.getOrPut("password") { mutableListOf() }.add("Не удалось получить Пароль")
        }
        if (errors.isNotEmpty()) {
            return errors
        }
        if (login != null && password != null) {
            if (!pattern.matches(login)) {
                errors.getOrPut("login") { mutableListOf() }
                    .add("Корректными для логина являются латинские буквы, цифры без пробелов и знаки: _ !")
            }
            if (login !in users.getListOfLogins()) {
                errors.getOrPut("login") { mutableListOf() }
                    .add("Несуществующий логин")
            } else if (users.fetchUser(login)?.password != Salt.getSalt(login)?.let { hashing(password, it) }) {
                println(users.fetchUser(login)?.password)
                println(Salt.getSalt(login)?.let { hashing(password, it) })
                errors.getOrPut("password") { mutableListOf() }.add("Неверный пароль")
            }
        }
        return errors
    }
}
