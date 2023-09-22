package ru.yarsu.web.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.template.PebbleTemplates
import ru.yarsu.web.models.NotFoundVM

val renderer = PebbleTemplates().HotReload("src/main/resources")

val notFoundFilter = Filter { handler: HttpHandler ->
    { request ->
        val response = handler(request)
        if (response.status == NOT_FOUND) {
            response.body(renderer(NotFoundVM(0)))
        } else {
            response
        }
    }
}
