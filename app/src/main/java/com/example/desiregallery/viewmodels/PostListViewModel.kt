package com.example.desiregallery.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
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

    fun getPosts(): LiveData<List<Post>> {
        loadPosts()
        return posts
    }

    private fun loadPosts() {
        val api = DGNetwork.getService()
        api.getPosts().enqueue(object: Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        posts.value = it
                        Log.i(TAG, "Posts have been loaded")
                    } ?: run { Log.i(TAG, "There are no posts to load") }
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Log.e(TAG, "Unable to load posts: ${t.message}")
            }
        })
    }
}
