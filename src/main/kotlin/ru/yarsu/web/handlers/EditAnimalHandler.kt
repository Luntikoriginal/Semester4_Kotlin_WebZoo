package ru.yarsu.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.body.form
import org.http4k.routing.path
import org.http4k.template.TemplateRenderer
import ru.yarsu.domain.Animal
import ru.yarsu.domain.ListOfAnimals
import ru.yarsu.web.models.EditAnimalVM
import ru.yarsu.web.validation.ValidatedFormFieldNewAnimal

fun showEditAnimalForm(renderer: TemplateRenderer, animals: ListOfAnimals): HttpHandler = { request: Request ->
    val page: Int? = request.path("page")?.toIntOrNull()
    val index: Int? = request.path("index")?.toIntOrNull()
    if (page == null || index == null || !animals.checkIndex(index)) {
        Response(Status.NOT_FOUND)
    } else {
        val currentAnimal = animals.fetchAnimal(index)
        Response(Status.OK).body(
            renderer(
                EditAnimalVM(
                    currentAnimal.type,
                    currentAnimal.name,
                    currentAnimal.description,
                    page,
                    index,
                ),
            ),
        )
    }
}

fun editAnimal(renderer: TemplateRenderer, animals: ListOfAnimals): HttpHandler = { request: Request ->
    val type: String? = request.form("type")
    val name: String? = request.form("name")
    val description: String? = request.form("description")
    val validator = ValidatedFormFieldNewAnimal(type, name, description)
    val page: Int? = request.path("page")?.toIntOrNull()
    val index: Int? = request.path("index")?.toIntOrNull()
    if (page == null || index == null || !animals.checkIndex(index)) {
        Response(Status.NOT_FOUND)
    } else {
        val errors = validator.validateForm()
        if (errors.isNotEmpty()) {
            Response(Status.BAD_REQUEST).body(renderer(EditAnimalVM(type, name, description, page, index, errors)))
        } else {
            val currentAnimal = animals.fetchAnimal(index)
            val editedAnimal = Animal(
                index,
                currentAnimal.datetime,
                type.toString(),
                name.toString(),
                description.toString(),
            )
            animals.editAnimal(currentAnimal, editedAnimal)
            Response(Status.FOUND).header("Location", "/animals/$page/animal/$index").body("")
        }
    }
}
