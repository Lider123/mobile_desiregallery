package com.example.desiregallery.ui.screens.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.network.BaseNetworkService
import com.example.desiregallery.data.network.RequestState
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PostListViewModel(
    application: Application,
    private val baseService: BaseNetworkService
) : AndroidViewModel(application) {
    private val postDataSourceFactory: PostDataSourceFactory = PostDataSourceFactory()

    var postsLiveData: LiveData<PagedList<Post>>

    init {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .setPrefetchDistance(3)
            .build()
        postsLiveData = LivePagedListBuilder(postDataSourceFactory, config).build()
    }

    private fun setState(state: RequestState) {
        postDataSourceFactory.postDataSourceLiveData.value?.updateState(state)
    }

    fun getState(): LiveData<RequestState> {
        return Transformations.switchMap<PostDataSource, RequestState>(
            postDataSourceFactory.postDataSourceLiveData,
            PostDataSource::state)
    }

    fun addPost(post: Post) {
        post.timestamp = Date().time
        baseService.createPost(post.id, post).enqueue(object: Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                logError(TAG, "Unable to create post ${post.id}: ${t.message}")
                setState(RequestState.ERROR_UPLOAD)
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    logError(TAG, "Unable to create post ${post.id}: received response with code ${response.code()}")
                    setState(RequestState.ERROR_UPLOAD)
                    return
                }
                logInfo(TAG, "Post ${post.id} successfully created")

                setState(RequestState.SUCCESS)
                postDataSourceFactory.postDataSourceLiveData.value?.invalidate()
            }
        })
    }

    fun updatePosts() {
        postDataSourceFactory.postDataSourceLiveData.value?.invalidate()
    }

    companion object {
        private val TAG = PostListViewModel::class.java.simpleName
        private const val PAGE_SIZE = 10
    }
}
