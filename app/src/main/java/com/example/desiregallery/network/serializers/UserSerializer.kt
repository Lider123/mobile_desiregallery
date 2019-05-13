package com.example.desiregallery.network.serializers

import com.example.desiregallery.models.User
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer

import java.lang.reflect.Type


class UserSerializer : JsonSerializer<User> {
    override fun serialize(src: User, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val result = JsonObject()
        val fields = JsonObject()
        val password = JsonObject()
        val email = JsonObject()
        val gender = JsonObject()
        val birthday = JsonObject()
        val photo = JsonObject()

        password.addProperty("stringValue", src.getPassword())
        email.addProperty("stringValue", src.getEmail())
        gender.addProperty("stringValue", src.getGender())
        birthday.addProperty("stringValue", src.getBirthday())
        photo.addProperty("stringValue", src.getPhoto())
        fields.add("password", password)
        fields.add("email", email)
        fields.add("gender", gender)
        fields.add("birthday", birthday)
        fields.add("photo", photo)
        result.add("fields", fields)

        return result
    }
}
