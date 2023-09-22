package ru.yarsu.web.models

import org.http4k.template.ViewModel
import ru.yarsu.web.validation.FormErrors

data class SignUpVM(
    val login: String?,
    val password: String?,
    val confirmPassword: String?,
    val errors: FormErrors? = null,
) : ViewModel
