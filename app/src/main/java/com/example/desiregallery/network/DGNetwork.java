package com.example.desiregallery.network;

import com.example.desiregallery.models.Post;
import com.example.desiregallery.models.User;
import com.example.desiregallery.network.serializers.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.reflect.Type;
import java.util.List;

public class DGNetwork {
    private static final String BASE_API = "https://firestore.googleapis.com/v1/projects/desiregallery-8072a/databases/(default)/documents/";

    private static IDGApi service;
    private static Gson gson;

    public static IDGApi getService() {
        if (service == null)
            initService();
        return service;
    }

    private static Gson getGson() {
        if (gson == null) {
            Type postListType = new TypeToken<List<Post>>() {}.getType();
            Type userListType = new TypeToken<List<User>>() {}.getType();
            GsonBuilder gsonBuilder = new GsonBuilder()
                    .registerTypeAdapter(Post.class, new PostDeserializer())
                    .registerTypeAdapter(postListType, new PostListDeserializer())
                    .registerTypeAdapter(User.class, new UserDeserializer())
                    .registerTypeAdapter(User.class, new UserSerializer())
                    .registerTypeAdapter(userListType, new UserListDeserializer());
            gson = gsonBuilder.create();
        }
        return gson;
    }

    private static void initService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_API)
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .build();
        service = retrofit.create(IDGApi.class);
    }
}
