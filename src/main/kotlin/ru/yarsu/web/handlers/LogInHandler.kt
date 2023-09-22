package ru.yarsu.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.body.form
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookie
import org.http4k.template.TemplateRenderer
import ru.yarsu.EXPIRATION
import ru.yarsu.domain.ListOfUsers
import ru.yarsu.web.models.LogInVM
import ru.yarsu.web.token.JwtTools
import ru.yarsu.web.validation.ValidatedFormFieldLogIn

fun showLogInForm(renderer: TemplateRenderer): HttpHandler = {
    Response(Status.OK).body(renderer(LogInVM("", "")))
}

fun logIn(renderer: TemplateRenderer, users: ListOfUsers): HttpHandler = { request: Request ->
    val login: String? = request.form("login")
    val password: String? = request.form("password")
    val validator = ValidatedFormFieldLogIn(login, password, users)
    val errors = validator.validateForm()
    if (errors.isNotEmpty()) {
        Response(Status.BAD_REQUEST).body(renderer(LogInVM(login, password, errors)))
    } else {
        val jwtTools = JwtTools("luntik", "zoo", EXPIRATION)
        val token = jwtTools.createToken(login)
        val cookie = Cookie("auth_token", token)
        Response(Status.FOUND).header("Location", "/").cookie(cookie).body("")
    }
}
