package com.example.desiregallery.network;

import com.example.desiregallery.models.Post;
import com.example.desiregallery.models.User;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Interface that defines REST api to communicate with server
 *
 * @author babaetskv
 * */
public interface IDGApi {
    /**
     * Method for getting all posts from server
     * */
    @GET("posts")
    Call<List<Post>> getPosts();

    /**
     * Method for getting all users from server. Is needed to get all logins for login validation
     * */
    @GET("users")
    Call<List<User>> getUsers();

    /**
     * Method for getting certain post
     *
     * @param id Unique identifier for post to return
     * */
    @GET("posts/{id}")
    Call<Post> getPost(@Path("id") String id);

    /**
     * Method for getting certain user
     *
     * @param login Login for user to return
     * */
    @GET("users/{login}")
    Call<User> getUser(@Path("login") String login);

    /**
     * Method that needs to create user on server
     *
     * @param login Unique login of user to write
     * @param user User data to write to server
     * */
    @POST("users")
    Call<User> createUser(@Query("documentId") String login, @Body User user);

    /**
     * Method that updates certain user on server
     *
     * @param login Login of user to update
     * @param user New user data
     * */
    @PATCH("users/{login}")
    Call<User> updateUser(@Path("login") String login, @Body User user);
}
