package ru.yarsu.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.BiDiLens
import org.http4k.template.TemplateRenderer
import ru.yarsu.domain.UserContext
import ru.yarsu.web.models.StartPageVM

fun showStartPage(
    renderer: TemplateRenderer,
    userContextLens: BiDiLens<Request, UserContext?>,
): HttpHandler = { request: Request ->
    Response(Status.OK).body(renderer(StartPageVM(userContextLens(request))))
}
