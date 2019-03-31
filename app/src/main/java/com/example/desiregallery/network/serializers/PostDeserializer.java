package com.example.desiregallery.network.serializers;

import com.example.desiregallery.models.Post;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PostDeserializer implements JsonDeserializer<Post> {

    @Override
    public Post deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject rootObject = json.getAsJsonObject();
        final JsonObject fieldsObject = rootObject.getAsJsonObject("fields");
        final JsonArray commentsObject = fieldsObject.getAsJsonObject("comments").getAsJsonObject("arrayValue").getAsJsonArray("values");

        String[] name = rootObject.get("name").getAsString().split("/");
        String id = name[name.length-1];

        String imageUrl = fieldsObject.getAsJsonObject("imageUrl").get("stringValue").getAsString();

        float rating;
        try {
            rating = fieldsObject.getAsJsonObject("rating").get("doubleValue").getAsFloat();
        } catch (NullPointerException e) {
            rating = fieldsObject.getAsJsonObject("rating").get("integerValue").getAsFloat();
        }

        int numOfRates = fieldsObject.getAsJsonObject("numOfRates").get("integerValue").getAsInt();

        List<String> comments = new ArrayList<>();
        if (commentsObject != null) {
            for (JsonElement el: commentsObject) {
                comments.add(el.getAsJsonObject().get("stringValue").getAsString());
            }
        }

        final Post post = new Post();
        post.setId(id);
        post.setImageUrl(imageUrl);
        post.setRating(rating);
        post.setNumOfRates(numOfRates);
        post.setComments(comments);
        return post;
    }
}
