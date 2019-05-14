package com.example.desiregallery.network;

import com.example.desiregallery.models.Post;
import com.example.desiregallery.models.User;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface IDGApi {
    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("users")
    Call<List<User>> getUsers();

    @GET("posts/{id}")
    Call<Post> getPost(@Path("id") String id);

    @GET("users/{login}")
    Call<User> getUser(@Path("login") String login);

    @POST("users")
    Call<User> createUser(@Query("documentId") String login, @Body User user);

    @PATCH("users/{login}")
    Call<User> updateUser(@Path("login") String login, @Body User user);
}
