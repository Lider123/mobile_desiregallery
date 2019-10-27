@file:JvmName("UsersProvider")
package com.example.desiregallery.helpers.mockmodels

import com.example.desiregallery.models.User

import java.util.ArrayList

private val users = initUsers()

private fun initUsers(): List<User> {
    val users = ArrayList<User>()

    var u = User("login1@example.com", "pass1")
    u.login = "login1"
    u.gender = "male"
    u.birthday = "01.01.1970"
    users.add(u)

    u = User("login2@example.com", "pass2")
    u.login = "login2"
    u.gender = "female"
    u.birthday = "21.12.2012"
    users.add(u)

    return users
}

fun getUser(login: String): User? = users.findLast{ it.login == login }
