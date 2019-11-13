package com.example.desiregallery.ui.screens.feed

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.network.BaseNetworkService
import com.example.desiregallery.data.network.NetworkUtils
import com.example.desiregallery.data.network.QueryNetworkService
import com.example.desiregallery.data.network.RequestState
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PostsViewModel(
    application: Application,
    private val baseService: BaseNetworkService,
    queryService: QueryNetworkService,
    networkUtils: NetworkUtils
) : AndroidViewModel(application) {
    private val postDataSourceFactory = PostsDataSource.Factory(queryService, networkUtils)

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
        return Transformations.switchMap<PostsDataSource, RequestState>(
            postDataSourceFactory.postDataSourceLiveData,
            PostsDataSource::state)
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
        private val TAG = PostsViewModel::class.java.simpleName
        private const val PAGE_SIZE = 10
    }

    class Factory(
        private val application: Application,
        private val baseService: BaseNetworkService,
        private val queryService: QueryNetworkService,
        private val networkUtils: NetworkUtils
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostsViewModel(application, baseService, queryService, networkUtils) as T
        }

    }
}
