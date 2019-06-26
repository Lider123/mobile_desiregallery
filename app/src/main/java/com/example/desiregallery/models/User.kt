package com.example.desiregallery.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Class that represents user
 *
 * Contains user's login, password and additional info
 *
 * @author babaetskv
 * */
open class User(
    /**
     * Users's login. Unique identifier of user
     * */
    @PrimaryKey
    private var login: String = "",

    /**
     * User's password
     * */
    private var password: String = ""
): RealmObject() {
    /**
     * User's email. Is used for password reset
     * */
    private var email = ""

    /**
     * User's gender. Is used for statistics
     * */
    private var gender = ""

    /**
     * User's birthday. Is used for statistics
     * */
    private var birthday = ""

    /**
     * User's photo in base64 string
     * */
    private var photo = ""

    fun getLogin() = login

    fun setLogin(login: String) {
        this.login = login
    }

    fun getPassword() = password

    fun setPassword(password: String) {
        this.password = password
    }

    fun getEmail() = email

    fun setEmail(email: String) {
        this.email = email
    }

    fun getGender() = gender

    fun setGender(gender: String) {
        this.gender = gender
    }

    fun getBirthday() = birthday

    fun setBirthday(birthday: String) {
        this.birthday = birthday
    }

    fun setPhoto(photo: String) {
        this.photo = photo
    }

    fun getPhoto() = photo

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