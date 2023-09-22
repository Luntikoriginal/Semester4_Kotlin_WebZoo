package ru.yarsu.web.models

import org.http4k.template.ViewModel
import ru.yarsu.domain.UserContext

data class StartPageVM(val userContext: UserContext?) : ViewModel
