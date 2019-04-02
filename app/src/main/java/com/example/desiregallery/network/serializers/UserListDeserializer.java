package com.example.desiregallery.network.serializers;

import com.example.desiregallery.models.User;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserListDeserializer implements JsonDeserializer<List<User>> {
    @Override
    public List<User> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<User> users = new ArrayList<>();
        JsonArray documents = json.getAsJsonObject().get("documents").getAsJsonArray();

        for (JsonElement el : documents) {
            User user = context.deserialize(el, User.class);
            users.add(user);
        }
        return users;
    }
}
