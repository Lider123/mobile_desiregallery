package com.example.desiregallery.network;

import com.example.desiregallery.models.Post;
import com.example.desiregallery.models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface IDGApi {
    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("posts/{id}")
    Call<Post> getPost(@Path("id") String id);

    @GET("users/{login}")
    Call<User> getUser(@Path("login") String login);
}
