package ru.yarsu.web.validation

typealias FormErrors = Map<String, List<String>>

data class ValidatedFormFieldNewAnimal(
    val type: String?,
    val name: String?,
    val description: String?,
) {
    fun validateForm(): FormErrors {
        val errors = mutableMapOf<String, MutableList<String>>()
        val headPattern = Regex("[а-яА-Я\\s-]+")
        val descriptionPattern = Regex("[а-яА-Яa-zA-Z0-9.,:;!?()\"'\\s-]+")
        if (type.isNullOrEmpty()) {
            errors.getOrPut("type") { mutableListOf() }.add("Не удалось получить Вид")
        }
        if (name.isNullOrEmpty()) {
            errors.getOrPut("name") { mutableListOf() }.add("Не удалось получить Имя")
        }
        if (description.isNullOrEmpty()) {
            errors.getOrPut("description") { mutableListOf() }.add("Не удалось получить Описание")
        }
        if (errors.isNotEmpty()) {
            return errors
        }
        if (type != null && name != null && description != null) {
            if (!headPattern.matches(type)) {
                errors.getOrPut("type") { mutableListOf() }.add("Вид содержит некорректные символы, возможно латиница")
            }
            if (type.first().isLowerCase()) {
                errors.getOrPut("type") { mutableListOf() }.add("Первая буква Вида должна быть заглавной")
            }
            if (!headPattern.matches(name)) {
                errors.getOrPut("name") { mutableListOf() }.add("Имя содержит некорректные символы, возможно латиница")
            }
            if (name.first().isLowerCase()) {
                errors.getOrPut("name") { mutableListOf() }.add("Первая буква Имени должна быть заглавной")
            }
            if (!descriptionPattern.matches(description)) {
                errors.getOrPut("description") { mutableListOf() }.add("Описание содержит некорректные символы")
            }
            if (description.first().isLowerCase()) {
                errors.getOrPut("description") { mutableListOf() }.add("Первая должна быть заглавной")
            }
        }
        return errors
    }
}
