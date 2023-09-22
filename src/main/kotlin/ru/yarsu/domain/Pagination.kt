package ru.yarsu.domain

data class Pagination<T>(private val animals: List<T>, private val elements: Int) {

    fun getPage(pageNumber: Int): List<T> {
        if (pageNumber in 1..getMaxPages()) {
            val startIndex = (pageNumber - 1) * elements
            val endIndex = startIndex + elements
            return animals.subList(startIndex, endIndex.coerceAtMost(animals.size))
        }
        return mutableListOf()
    }

    fun getMaxPages(): Int {
        return (animals.size + elements - 1) / elements
    }

    fun checkPage(page: Int): Boolean {
        return page in 1..getMaxPages()
    }
}
