package com.example.desiregallery.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.desiregallery.logging.DGLogger
import com.example.desiregallery.models.Post
import com.example.desiregallery.network.DGNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PostListViewModel : ViewModel() {
    companion object {
        private val TAG = PostListViewModel::class.java.simpleName
    }

    private var posts = MutableLiveData<List<Post>>()

    init {
        loadPosts()
    }

    fun getPosts() = posts

    private fun loadPosts() {
        DGNetwork.baseService.getPosts().enqueue(object: Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        posts.value = response.body()
                        DGLogger.logInfo(TAG, "Posts have been loaded")
                    }
                    else {
                        DGLogger.logInfo(TAG, "There are no posts to load")
                    }
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                DGLogger.logError(TAG, "Unable to load posts: ${t.message}")
            }
        })
    }

    fun addPost(post: Post) {
        val currPosts = posts.value as MutableList
        currPosts.add(0, post)
        posts.value = currPosts
        DGNetwork.baseService.createPost(post.id, post).enqueue(object: Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                DGLogger.logError(TAG, "Unable to create post ${post.id}: ${t.message}")
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful)
                    DGLogger.logError(TAG, "Unable to create post ${post.id}: received response with code ${response.code()}")
                else
                    DGLogger.logInfo(TAG, "Post ${post.id} successfully created")
            }
        })
    }
}
