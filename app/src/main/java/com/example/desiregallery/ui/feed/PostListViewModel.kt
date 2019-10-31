package com.example.desiregallery.ui.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
import com.example.desiregallery.utils.logWarning
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.network.IStatusHandler
import com.example.desiregallery.data.network.RequestStatus
import com.example.desiregallery.data.network.baseService
import com.example.desiregallery.data.network.query.OrderDirection
import com.example.desiregallery.data.network.query.QueryRequest
import com.example.desiregallery.data.network.queryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class PostListViewModel(application: Application) : AndroidViewModel(application), IStatusHandler {
    private val postDataSourceFactory: PostDataSourceFactory = PostDataSourceFactory(this)
    private val postDataSourceLiveData: MutableLiveData<PostDataSource>
    private val executor: Executor
    val pagedListLiveData: LiveData<PagedList<Post>>
    var requestStatus = MutableLiveData<RequestStatus>()

    init {
        postDataSourceLiveData = postDataSourceFactory.mutableLiveData

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .setPrefetchDistance(3)
            .build()
        executor = Executors.newFixedThreadPool(5)
        pagedListLiveData = LivePagedListBuilder(postDataSourceFactory, config)
            .setFetchExecutor(executor)
            .build()
    }

    override fun setRequestStatus(status: RequestStatus) {
        requestStatus.postValue(status)
    }

    /*fun loadPosts() {
        val offset = (PAGE_SIZE * (1-1)).toLong()
        val query = QueryRequest()
            .from("posts")
            .orderBy("timestamp", OrderDirection.DESCENDING)
            .limit(PAGE_SIZE)
            .offset(offset)
        isLoading.value = true
        queryService.getPosts(query).enqueue(object: Callback<List<Post>> {

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful)
                    logWarning(TAG, "Failed to load posts. Response received with code ${response.code()}: ${response.message()}")
                else if (response.body() == null)
                    logWarning(TAG, "Failed to load posts. Response received an empty body")
                else {
                    posts.value = response.body() as MutableList<Post>
                    logInfo(TAG, "Posts have been loaded")
                }
                isLoading.value = false
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                logError(TAG, "Unable to load posts: ${t.message}")
                isLoading.value = false
            }
        })
    }*/

    fun addPost(post: Post) {
        post.timestamp = Date().time
        baseService.createPost(post.id, post).enqueue(object: Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                logError(TAG, "Unable to create post ${post.id}: ${t.message}")
                setRequestStatus(RequestStatus.ERROR_UPLOAD)
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    logError(TAG, "Unable to create post ${post.id}: received response with code ${response.code()}")
                    setRequestStatus(RequestStatus.ERROR_UPLOAD)
                    return
                }
                logInfo(TAG, "Post ${post.id} successfully created")

                setRequestStatus(RequestStatus.SUCCESS)
                postDataSourceLiveData.value?.invalidate()
            }
        })
    }

    companion object {
        private val TAG = PostListViewModel::class.java.simpleName
        private const val PAGE_SIZE = 10
    }
}
