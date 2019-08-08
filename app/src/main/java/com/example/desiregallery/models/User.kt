package com.example.desiregallery.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User(
    @PrimaryKey
    private var login: String = "",
    var password: String = ""
): RealmObject() {

    var email = ""
    var gender = ""
    var birthday = ""
    var photo = ""

    fun getLogin() = login

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other !is User)
            return false

        if (login != other.login)
            return false
        if (password != other.password)
            return false
        if (email != other.email)
            return false
        if (gender != other.gender)
            return false
        if (birthday != other.birthday)
            return false

        return true
    }

    override fun hashCode(): Int {
        var result = login.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + birthday.hashCode()
        return result
    }
}