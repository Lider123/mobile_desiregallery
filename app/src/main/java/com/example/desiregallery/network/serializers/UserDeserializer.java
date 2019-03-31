package com.example.desiregallery.network.serializers;

import com.example.desiregallery.models.User;
import com.google.gson.*;

import java.lang.reflect.Type;

public class UserDeserializer implements JsonDeserializer<User> {
    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject rootObject = json.getAsJsonObject();
        final JsonObject fieldsObject = rootObject.getAsJsonObject("fields");

        String[] name = rootObject.get("name").getAsString().split("/");
        String login = name[name.length-1];

        String password = fieldsObject.getAsJsonObject("password").get("stringValue").getAsString();

        return new User(login, password);
    }
}
