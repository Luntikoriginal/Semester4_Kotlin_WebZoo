package ru.yarsu.domain

import java.time.LocalDateTime

data class Animal(
    private var index: Int,
    val datetime: LocalDateTime,
    val type: String,
    val name: String,
    val description: String,
) {
    fun setIndex(i: Int) {
        index = i
    }

    fun getIndex(): Int = index
}
