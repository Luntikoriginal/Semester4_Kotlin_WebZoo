package ru.yarsu.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.path
import org.http4k.template.TemplateRenderer
import ru.yarsu.domain.ListOfAnimals
import ru.yarsu.web.models.DeleteAnimalVM

fun showDeleteAnimalForm(renderer: TemplateRenderer, animals: ListOfAnimals): HttpHandler = {
        request: Request ->
    val page: Int? = request.path("page")?.toIntOrNull()
    val index: Int? = request.path("index")?.toIntOrNull()
    if (page == null || index == null || !animals.checkIndex(index)) {
        Response(Status.NOT_FOUND)
    } else {
        val currentAnimal = animals.fetchAnimal(index)
        Response(Status.OK).body(renderer(DeleteAnimalVM(currentAnimal, page, index)))
    }
}

fun deleteAnimal(animals: ListOfAnimals): HttpHandler = { request: Request ->
    val index: Int? = request.path("index")?.toIntOrNull()
    if (index == null || !animals.checkIndex(index)) {
        Response(Status.NOT_FOUND)
    } else {
        animals.deleteAnimal(animals.fetchAnimal(index))
        animals.sorting()
        Response(Status.FOUND).header("Location", "/").body("")
    }
}
