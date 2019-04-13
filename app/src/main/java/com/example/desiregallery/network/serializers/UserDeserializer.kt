package com.example.desiregallery.network.serializers

import com.example.desiregallery.models.User
import com.google.gson.*

import java.lang.reflect.Type

class UserDeserializer : JsonDeserializer<User> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): User  {
        val rootObject = json.asJsonObject
        val fieldsObject = rootObject.getAsJsonObject("fields")

        val name = rootObject.get("name").asString.split("/")
        val login = name[name.size-1]

        val password = fieldsObject.getAsJsonObject("password").get("stringValue").asString

        val email = fieldsObject.getAsJsonObject("email").get("stringValue").asString

        val gender = fieldsObject.getAsJsonObject("gender").get("stringValue").asString

        val birthday = fieldsObject.getAsJsonObject("birthday").get("stringValue").asString

        val user = User(login, password)
        user.setEmail(email)
        user.setGender(gender)
        user.setBirthday(birthday)
        return user
    }
}
