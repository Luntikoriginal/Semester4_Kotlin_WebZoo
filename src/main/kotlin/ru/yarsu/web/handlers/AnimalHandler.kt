package ru.yarsu.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.path
import org.http4k.template.TemplateRenderer
import ru.yarsu.domain.ListOfAnimals
import ru.yarsu.web.models.AnimalVM

fun showAnimal(renderer: TemplateRenderer, animals: ListOfAnimals): HttpHandler = { request ->
    val page: Int? = request.path("page")?.toIntOrNull()
    val index: Int? = request.path("index")?.toIntOrNull()
    if (page == null || index == null || !animals.checkIndex(index)) {
        Response(Status.NOT_FOUND)
    } else {
        Response(Status.OK).body(renderer(AnimalVM(animals.fetchAnimal(index), page)))
    }
}
