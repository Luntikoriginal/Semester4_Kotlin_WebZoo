package ru.yarsu.web.filters

import org.http4k.core.Filter
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.BiDiLens
import ru.yarsu.domain.ListOfUsers
import ru.yarsu.domain.UserContext
import ru.yarsu.web.models.UnauthorizedVM
import ru.yarsu.web.permission.Permission

class PermissionFilter(private val users: ListOfUsers) {

    fun filterPermission(
        permissionLens: BiDiLens<Request, UserContext?>,
        canUse: (Permission) -> Boolean,
    ) = Filter { handler ->
        { request ->
            var permission = Permission("guest", canCreateNewAnimal = false, canManageUsers = false)
            val userContext = permissionLens(request)
            if (userContext != null) {
                val login = userContext.login
                val currentUser = users.fetchUser(login)
                if (currentUser != null) {
                    permission = currentUser.rolePermissions
                }
            }
            if (!canUse(permission)) {
                Response(Status.UNAUTHORIZED).body(renderer(UnauthorizedVM(0)))
            } else {
                handler(request)
            }
        }
    }
}
