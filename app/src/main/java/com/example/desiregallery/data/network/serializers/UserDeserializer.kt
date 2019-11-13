package com.example.desiregallery.data.network.serializers

import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.FIELDS
import com.example.desiregallery.data.network.NAME
import com.example.desiregallery.data.network.STRING_VALUE
import com.google.gson.*

import java.lang.reflect.Type

class UserDeserializer : JsonDeserializer<User> {

    override fun deserialize(json: JsonElement, typeOfT: Type,
                             context: JsonDeserializationContext): User  {
        val rootObject = json.asJsonObject
        val fieldsObject = rootObject.getAsJsonObject(FIELDS)

        val login = rootObject.get(NAME).asString.split("/").last()
        val password = fieldsObject.getAsJsonObject("password").get(STRING_VALUE)
            .asString
        val email = fieldsObject.getAsJsonObject("email").get(STRING_VALUE).asString
        val birthday = fieldsObject.getAsJsonObject("birthday").get(STRING_VALUE)
            .asString
        val photo = fieldsObject.getAsJsonObject("photo").get(STRING_VALUE).asString

        return User(email, password).also {
            it.login = login
            it.birthday = birthday
            it.photo = photo
        }
    }
}
