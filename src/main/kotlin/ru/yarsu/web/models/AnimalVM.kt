package ru.yarsu.web.models

import org.http4k.template.ViewModel
import ru.yarsu.domain.Animal

data class AnimalVM(
    val animal: Animal,
    val page: Int = 0,
) : ViewModel
