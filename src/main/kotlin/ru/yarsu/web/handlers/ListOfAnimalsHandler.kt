package ru.yarsu.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.path
import org.http4k.template.TemplateRenderer
import ru.yarsu.domain.ListOfAnimals
import ru.yarsu.domain.Pagination
import ru.yarsu.web.models.ListOfAnimalsVM

const val TEN = 10

fun showListOfAnimals(renderer: TemplateRenderer, animals: ListOfAnimals): HttpHandler = { request ->
    val types = animals.getListOfTypes()

    val page: Int? = request.path("page")?.toIntOrNull()
    if (page == null || page < 1) {
        Response(Status.NOT_FOUND)
    } else {
        val boxYears = animals.getBoundsYears()
        val minYear = boxYears[0]
        val maxYear = boxYears[1]
        var startYear = request.query("startYear")?.toIntOrNull() ?: minYear
        var endYear = request.query("endYear")?.toIntOrNull() ?: maxYear
        if (startYear > endYear) {
            val temp = startYear
            startYear = endYear
            endYear = temp
        }

        val filterType: String? = request.query("filterType")
        if (filterType == null) {
            Response(Status.NOT_FOUND)
        } else {
            val filteredAnimals = animals.filterByParameters(filterType, startYear, endYear)

            val pagination = Pagination(filteredAnimals, TEN)
            val maxPages = pagination.getMaxPages()
            val nextPage = "/animals/${page + 1}?filterType=$filterType&startYear=$startYear&endYear=$endYear"
            val backPage = "/animals/${page - 1}?filterType=$filterType&startYear=$startYear&endYear=$endYear"
            val firstPage = "/animals/1?filterType=$filterType&startYear=$startYear&endYear=$endYear"
            val lastPage = "/animals/$maxPages?filterType=$filterType&startYear=$startYear&endYear=$endYear"
            val currentListPage = pagination.getPage(page)
            Response(Status.OK).body(
                renderer(
                    ListOfAnimalsVM(
                        currentListPage,
                        types,
                        filterType,
                        page,
                        nextPage,
                        backPage,
                        firstPage,
                        lastPage,
                        maxPages,
                        startYear,
                        endYear,
                    ),
                ),
            )
        }
    }
}
