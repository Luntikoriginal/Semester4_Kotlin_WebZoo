package ru.yarsu.web.validation

import ru.yarsu.EIGHT

data class ValidatedFormFieldSignUp(
    val login: String?,
    val password: String?,
    val confirmPassword: String?,
    val logins: List<String>,
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
        if (confirmPassword.isNullOrEmpty()) {
            errors.getOrPut("confirmPassword") { mutableListOf() }.add("Не удалось получить Подтверждение пароля")
        }
        if (errors.isNotEmpty()) {
            return errors
        }
        if (login != null && password != null) {
            if (!pattern.matches(login)) {
                errors.getOrPut("login") { mutableListOf() }
                    .add("Корректными являются латинские буквы, цифры без пробелов и знаки: - _ !")
            }
            if (login in logins) {
                errors.getOrPut("login") { mutableListOf() }
                    .add("Логин уже занят")
            }
            if (!pattern.matches(password)) {
                errors.getOrPut("password") { mutableListOf() }
                    .add("Корректными являются латинские буквы, цифры без пробелов и знаки: - _ !")
            }
            if (password.length < EIGHT) {
                errors.getOrPut("password") { mutableListOf() }
                    .add("Пароль должен состоять минимум из 8 символов")
            }
            if (password != confirmPassword) {
                errors.getOrPut("confirmPassword") { mutableListOf() }
                    .add("Введенные пароли не совпадают")
            }
        }
        return errors
    }
}
