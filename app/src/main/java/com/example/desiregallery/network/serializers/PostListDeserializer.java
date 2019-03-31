package com.example.desiregallery.network.serializers;

import com.example.desiregallery.models.Post;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PostListDeserializer implements JsonDeserializer<List<Post>> {
    @Override
    public List<Post> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Post> posts = new ArrayList<Post>();
        JsonArray documents = json.getAsJsonObject().get("documents").getAsJsonArray();

        for (JsonElement el : documents) {
            Post post = context.deserialize(el, Post.class);
            posts.add(post);
        }
        return posts;
    }
}
