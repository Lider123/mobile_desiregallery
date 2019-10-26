package com.example.desiregallery.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.desiregallery.logging.DGLogger
import com.example.desiregallery.models.Post
import com.example.desiregallery.network.DGNetwork
import com.example.desiregallery.network.query.OrderDirection
import com.example.desiregallery.network.query.QueryRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PostListViewModel : ViewModel() {
    val posts = MutableLiveData<MutableList<Post>>()
    val isLoading = MutableLiveData<Boolean>().apply { value = false }

    var currentPage = 0
    var isLastPage = false
    val pageSize: Int
        get() = PAGE_SIZE

    init {
        loadPosts()
    }

    fun loadPosts() {
        val query = buildQuery(currentPage+1)
        isLoading.value = true
        DGNetwork.queryService.getPosts(query).enqueue(object: Callback<List<Post>> {

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful)
                    DGLogger.logWarning(TAG, "Failed to load posts. Response received with code ${response.code()}: ${response.message()}")
                else if (response.body() == null)
                    DGLogger.logWarning(TAG, "Failed to load posts. Response received an empty body")
                else if (response.body()!!.isEmpty()) {
                    isLastPage = true
                    DGLogger.logInfo(TAG, "Last page reached")
                }
                else {
                    posts.value = response.body() as MutableList<Post>
                    DGLogger.logInfo(TAG, "Posts have been loaded")
                    currentPage++
                }
                isLoading.value = false
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                DGLogger.logError(TAG, "Unable to load posts: ${t.message}")
                isLoading.value = false
            }
        })
    }

    private fun buildQuery(page: Int): QueryRequest {
        val offset = PAGE_SIZE * (page-1)
        return QueryRequest()
            .from("posts")
            .orderBy("timestamp", OrderDirection.DESCENDING)
            .limit(PAGE_SIZE)
            .offset(offset)
    }

    fun addPost(post: Post) {
        val currPosts = posts.value as MutableList
        currPosts.add(0, post.apply { timestamp = Date().time })
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

    companion object {
        private val TAG = PostListViewModel::class.java.simpleName
        const val PAGE_SIZE = 10
    }
}
