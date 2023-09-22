package ru.yarsu.web.models

import org.http4k.template.ViewModel
import ru.yarsu.domain.Animal

data class ListOfAnimalsVM(
    val animals: List<Animal>,
    val types: MutableList<String>,
    val currentType: String,
    val pageNumber: Int,
    val nextPage: String,
    val backPage: String,
    val firstPage: String,
    val lastPage: String,
    val maxPage: Int,
    val startYear: Int,
    val endYear: Int,
) : ViewModel
