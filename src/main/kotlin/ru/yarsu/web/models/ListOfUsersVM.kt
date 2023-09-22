package ru.yarsu.web.models

import org.http4k.template.ViewModel
import ru.yarsu.domain.User

data class ListOfUsersVM(val users: List<User>) : ViewModel
