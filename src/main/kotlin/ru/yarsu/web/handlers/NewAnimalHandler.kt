package ru.yarsu.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.body.form
import org.http4k.template.TemplateRenderer
import ru.yarsu.domain.Animal
import ru.yarsu.domain.ListOfAnimals
import ru.yarsu.web.models.AnimalVM
import ru.yarsu.web.models.NewAnimalVM
import ru.yarsu.web.validation.ValidatedFormFieldNewAnimal
import java.time.LocalDateTime

fun showNewAnimalForm(renderer: TemplateRenderer): HttpHandler = {
    Response(Status.OK).body(renderer(NewAnimalVM("", "", "")))
}

fun createNewAnimal(renderer: TemplateRenderer, animals: ListOfAnimals): HttpHandler = { request: Request ->
    val type: String? = request.form("type")
    val name: String? = request.form("name")
    val description: String? = request.form("description")
    val validator = ValidatedFormFieldNewAnimal(type, name, description)
    val errors = validator.validateForm()
    if (errors.isNotEmpty()) {
        Response(Status.BAD_REQUEST).body(renderer(NewAnimalVM(type, name, description, errors)))
    } else {
        var index = 0
        if (animals.fetchAllAnimals().size > 0) {
            index = animals.fetchAllAnimals().last().getIndex() + 1
        }
        val newAnimal = Animal(
            index,
            LocalDateTime.now(),
            type.toString(),
            name.toString(),
            description.toString(),
        )
        animals.add(newAnimal)
        animals.sorting()
        Response(Status.OK).body(renderer(AnimalVM(newAnimal)))
    }
}
