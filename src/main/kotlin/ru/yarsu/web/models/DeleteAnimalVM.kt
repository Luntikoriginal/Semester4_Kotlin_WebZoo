package ru.yarsu.web.models

import org.http4k.template.ViewModel
import ru.yarsu.domain.Animal

data class DeleteAnimalVM(
    val animal: Animal,
    val page: Int,
    val index: Int,
) : ViewModel
