package ru.yarsu.web.models

import org.http4k.template.ViewModel
import ru.yarsu.web.validation.FormErrors

class LogInVM(val login: String?, val password: String?, val errors: FormErrors? = null) : ViewModel
