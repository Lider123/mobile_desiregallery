package com.example.desiregallery.network.serializers;

import com.example.desiregallery.models.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;


public class UserSerializer implements JsonSerializer<User> {
    @Override
    public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        JsonObject fields = new JsonObject();
        JsonObject password = new JsonObject();

        password.addProperty("stringValue", src.getPassword());
        fields.add("password", password);
        result.add("fields", fields);

        return result;
    }
}
