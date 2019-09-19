package com.example.desiregallery.models

class User(
    val email: String,
    val password: String
) {

    var login = ""
    var gender = ""
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

    override fun toString(): String {
        return "User(email='$email', password='$password', login='$login', gender='$gender', birthday='$birthday', photo='$photo')"
    }
}
