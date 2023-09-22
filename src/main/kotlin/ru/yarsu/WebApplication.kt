package ru.yarsu

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.RequestContexts
import org.http4k.core.Response
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.cookie.cookie
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.ServerFilters
import org.http4k.lens.BiDiLens
import org.http4k.lens.RequestContextKey
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.Netty
import org.http4k.server.asServer
import org.http4k.template.PebbleTemplates
import org.http4k.template.TemplateRenderer
import ru.yarsu.domain.Animal
import ru.yarsu.domain.ListOfAnimals
import ru.yarsu.domain.ListOfUsers
import ru.yarsu.domain.Salt
import ru.yarsu.domain.User
import ru.yarsu.domain.UserContext
import ru.yarsu.domain.operations.hashing
import ru.yarsu.web.filters.PermissionFilter
import ru.yarsu.web.filters.notFoundFilter
import ru.yarsu.web.handlers.createNewAnimal
import ru.yarsu.web.handlers.deleteAnimal
import ru.yarsu.web.handlers.editAnimal
import ru.yarsu.web.handlers.logIn
import ru.yarsu.web.handlers.showAnimal
import ru.yarsu.web.handlers.showDeleteAnimalForm
import ru.yarsu.web.handlers.showEditAnimalForm
import ru.yarsu.web.handlers.showListOfAnimals
import ru.yarsu.web.handlers.showListOfUsers
import ru.yarsu.web.handlers.showLogInForm
import ru.yarsu.web.handlers.showNewAnimalForm
import ru.yarsu.web.handlers.showSignUpForm
import ru.yarsu.web.handlers.showStartPage
import ru.yarsu.web.handlers.signUp
import ru.yarsu.web.models.FileReadErrorVM
import ru.yarsu.web.permission.Permission
import ru.yarsu.web.permission.RolePermissions
import ru.yarsu.web.token.JwtTools
import java.io.File
import java.io.IOException
import java.time.LocalDateTime

const val EXPIRATION = 1800000L
const val THREE = 3
const val FOUR = 4
const val FIVE = 5
const val SIX = 6
const val SEVEN = 7
const val EIGHT = 8
const val PORT = 9000

fun router(
    renderer: TemplateRenderer,
    animals: ListOfAnimals,
    users: ListOfUsers,
    userContextLens: BiDiLens<Request, UserContext?>,
): HttpHandler = routes(
    "/" bind GET to showStartPage(renderer, userContextLens),
    "/animals/{page}" bind GET to showListOfAnimals(renderer, animals),
    "/animals/{page}/animal/{index}" bind GET to showAnimal(renderer, animals),
    "/animal/create" bind GET to showNewAnimalForm(renderer),
    /*  PermissionFilter(users)
        .filterPermission(userContextLens, Permission::canCreateNewAnimal)
        .then()  */
    "/animal/create" bind POST to createNewAnimal(renderer, animals),
    "/animals/{page}/animal/{index}/edit" bind GET to showEditAnimalForm(renderer, animals),
    "/animals/{page}/animal/{index}/edit" bind POST to editAnimal(renderer, animals),
    "/animals/{page}/animal/{index}/delete" bind GET to showDeleteAnimalForm(renderer, animals),
    "/animals/{page}/animal/{index}/delete" bind POST to deleteAnimal(animals),
    "/users" bind GET to showListOfUsers(renderer, users),
    "/user/signUp" bind GET to showSignUpForm(renderer),
    "/user/signUp" bind POST to signUp(renderer, users),
    "/user/logIn" bind GET to showLogInForm(renderer),
    "/user/logIn" bind POST to logIn(renderer, users),
    static(ResourceLoader.Classpath("/ru/yarsu/web/public/")),
).withFilter(notFoundFilter)

fun main() {
    val animals = ListOfAnimals(mutableListOf())
    val users = ListOfUsers(mutableListOf())

    val userContexts = RequestContexts()
    val userContextLens = RequestContextKey.optional<UserContext>(userContexts)

    fun filtered(key: BiDiLens<Request, UserContext?>) = Filter { handler ->
        { request ->
            val token = request.cookie("auth_token")
            if (token != null) {
                val jwt = JwtTools("luntik", "zoo", EXPIRATION)
                val login = jwt.getLogin(token)
                if (login != null) {
                    val userContext = users.createUserContext(login)
                    handler(request.with(key of userContext))
                } else {
                    handler(request)
                }
            } else {
                handler(request)
            }
        }
    }

    val initContextFilter = ServerFilters.InitialiseRequestContext(userContexts)
        .then(filtered(userContextLens))

    users.add(
        User(
            "admin_login",
            hashing("12345678", Salt.generateSalt("admin_login")),
            LocalDateTime.now(),
            RolePermissions.ADMINISTRATOR,
        ),
    )
    val datafile = File("src/main/resources/ru/yarsu/storage/animal_data.csv")
    try {
        val dataAnimals = datafile.readLines()
        for (animal in dataAnimals) {
            val elements = animal.split(",")
            val datetime = LocalDateTime.of(
                elements[1].toInt(),
                elements[2].toInt(),
                elements[THREE].toInt(),
                elements[FOUR].toInt(),
                elements[FIVE].toInt(),
            )
            animals.add(Animal(elements[0].toInt(), datetime, elements[SIX], elements[SEVEN], elements[EIGHT]))
        }
        animals.sorting()
        val renderer = PebbleTemplates().HotReload("src/main/resources")
        val app = router(renderer, animals, users, userContextLens)
        val contextApp: HttpHandler = initContextFilter.then(app)
        val server = contextApp.asServer(Netty(PORT)).start()
        println("Server started on http://localhost:" + server.port())
    } catch (fileReadException: IOException) {
        val renderer = PebbleTemplates().HotReload("src/main/resources")
        val errorPage = routes(
            "/" bind GET to {
                Response(INTERNAL_SERVER_ERROR).body(renderer(FileReadErrorVM(0)))
            },
            static(ResourceLoader.Classpath("/ru/yarsu/web/public/")),
        )
        val server = errorPage.asServer(Netty(PORT)).start()
        println("Server started on http://localhost:" + server.port())
        fileReadException.addSuppressed(fileReadException)
    }
}
