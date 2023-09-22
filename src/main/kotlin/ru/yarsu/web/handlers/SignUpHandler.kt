package ru.yarsu.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.body.form
import org.http4k.template.TemplateRenderer
import ru.yarsu.domain.ListOfUsers
import ru.yarsu.domain.Salt
import ru.yarsu.domain.User
import ru.yarsu.domain.operations.hashing
import ru.yarsu.web.models.SignUpVM
import ru.yarsu.web.permission.RolePermissions
import ru.yarsu.web.validation.ValidatedFormFieldSignUp
import java.time.LocalDateTime

fun showSignUpForm(renderer: TemplateRenderer): HttpHandler = {
    Response(Status.OK).body(renderer(SignUpVM("", "", "")))
}

fun signUp(renderer: TemplateRenderer, users: ListOfUsers): HttpHandler = { request: Request ->
    val login: String? = request.form("login")
    val password: String? = request.form("password")
    val confirmPassword: String? = request.form("confirmPassword")
    val validator = ValidatedFormFieldSignUp(login, password, confirmPassword, users.getListOfLogins())
    val errors = validator.validateForm()
    if (errors.isNotEmpty()) {
        Response(Status.BAD_REQUEST).body(renderer(SignUpVM(login, password, confirmPassword, errors)))
    } else {
        val newUser = User(
            login.toString(),
            hashing(password.toString(), Salt.generateSalt(login.toString())),
            LocalDateTime.now(),
            RolePermissions.USER,
        )
        users.add(newUser)
        Response(Status.FOUND).header("Location", "/users").body("")
    }
}
