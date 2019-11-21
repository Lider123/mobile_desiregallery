package com.example.desiregallery.data.network.serializers

import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.FIELDS
import com.example.desiregallery.data.network.STRING_VALUE
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer

import java.lang.reflect.Type

class UserSerializer : JsonSerializer<User> {

    override fun serialize(
        src: User,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val result = JsonObject()
        val fields = JsonObject()
        val password = JsonObject()
        val email = JsonObject()
        val birthday = JsonObject()
        val photo = JsonObject()

        password.addProperty(STRING_VALUE, src.password)
        email.addProperty(STRING_VALUE, src.email)
        birthday.addProperty(STRING_VALUE, src.birthday)
        photo.addProperty(STRING_VALUE, src.photo)

        with(fields) {
            add("password", password)
            add("email", email)
            add("birthday", birthday)
            add("photo", photo)
        }
        result.add(FIELDS, fields)
        return result
    }
}
