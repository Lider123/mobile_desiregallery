package com.example.desiregallery.data.models

import java.io.Serializable

class User(val email: String, val password: String) : Serializable {
    var login = ""
    var birthday = ""
    var photo = ""

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
        if (birthday != other.birthday)
            return false
        if (photo != other.photo)
            return false

        return true
    }

    override fun hashCode(): Int {
        var result = login.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + birthday.hashCode()
        result = 31 * result + photo.hashCode()
        return result
    }

    override fun toString(): String {
        return "User(" +
                "email='$email', " +
                "password='$password', " +
                "login='$login', " +
                "birthday='$birthday', " +
                "photo='$photo')"
    }
}
