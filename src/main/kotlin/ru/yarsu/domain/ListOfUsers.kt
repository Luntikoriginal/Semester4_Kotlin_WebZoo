package ru.yarsu.domain

data class ListOfUsers(private var listOfUsers: MutableList<User>) {

    fun add(user: User) {
        listOfUsers.add(user)
    }

    fun fetchUser(login: String): User? {
        return listOfUsers.find { it.login == login }
    }

    fun fetchAllUsers(): MutableList<User> = listOfUsers

    fun getListOfLogins(): MutableList<String> {
        val listOfLogins = mutableListOf<String>()
        for (user in listOfUsers) {
            listOfLogins.add(user.login)
        }
        return listOfLogins
    }

    fun createUserContext(login: String): UserContext? {
        val user = fetchUser(login)
        if (user != null) {
            print(user.login)
            return UserContext(user.login)
        }
        return null
    }
}
