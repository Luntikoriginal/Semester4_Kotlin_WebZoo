package ru.yarsu.web.models

import org.http4k.template.ViewModel
import ru.yarsu.web.validation.FormErrors

data class EditAnimalVM(
    val type: String?,
    val name: String?,
    val description: String?,
    val page: Int,
    val index: Int,
    val errors: FormErrors? = null,
) : ViewModel
