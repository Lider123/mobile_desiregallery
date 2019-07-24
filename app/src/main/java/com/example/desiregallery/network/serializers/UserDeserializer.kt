package com.example.desiregallery.network.serializers

import com.example.desiregallery.models.User
import com.google.gson.*

import java.lang.reflect.Type

class UserDeserializer : JsonDeserializer<User> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): User  {
        val rootObject = json.asJsonObject
        val fieldsObject = rootObject.getAsJsonObject("fields")

        val login = rootObject.get("name").asString.split("/").last()

        val password = fieldsObject.getAsJsonObject("password").get("stringValue").asString

        val email = fieldsObject.getAsJsonObject("email").get("stringValue").asString

        val gender = fieldsObject.getAsJsonObject("gender").get("stringValue").asString

        val birthday = fieldsObject.getAsJsonObject("birthday").get("stringValue").asString

        val photo = fieldsObject.getAsJsonObject("photo").get("stringValue").asString

        return User(login, password).apply {
            setEmail(email)
            setGender(gender)
            setBirthday(birthday)
            setPhoto(photo)
        }
    }
}
