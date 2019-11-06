package com.example.desiregallery.ui.screens.feed

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.network.QueryNetworkService
import com.example.desiregallery.data.network.RequestState
import com.example.desiregallery.data.network.query.requests.PostsQueryRequest
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
import com.example.desiregallery.utils.logWarning
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author babaetskv on 31.10.19
 */
class PostDataSource : PageKeyedDataSource<Long, Post>(), KoinComponent {
    private val networkService: QueryNetworkService by inject()

    var state: MutableLiveData<RequestState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Post>) {
        updateState(RequestState.DOWNLOADING)
        val query = PostsQueryRequest(
            params.requestedLoadSize,
            0
        )
        networkService.getPosts(query).enqueue(object: Callback<List<Post>> {

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                logError(TAG, "Unable to load posts for page 1: ${t.message}")
                updateState(RequestState.ERROR_DOWNLOAD)
            }

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    logWarning(TAG, "Failed to load posts for page 1. Received code ${response.code()}: ${response.message()}")
                    updateState(RequestState.ERROR_DOWNLOAD)
                    return
                }
                val posts = response.body()
                posts?.let {
                    if (posts.isEmpty()) {
                        logInfo(TAG, "There are no posts to download")
                        updateState(RequestState.NO_DATA)
                    }
                    else {
                        logInfo(TAG, "Successfully loaded ${it.size} posts for page 1")
                        updateState(RequestState.SUCCESS)
                    }
                    callback.onResult(it, null, 2L)
                }?: logWarning(TAG, "Failed to load posts for page 1. Received an empty body")
            }
        })
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Post>) {
        val key = params.key
        val pageSize = params.requestedLoadSize
        updateState(RequestState.DOWNLOADING)
        val query = PostsQueryRequest(
            pageSize,
            pageSize * (key - 1)
        )
        networkService.getPosts(query).enqueue(object: Callback<List<Post>> {

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                logError(TAG, "Unable to load posts for page $key: ${t.message}")
                updateState(RequestState.ERROR_DOWNLOAD)
            }

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    logWarning(TAG, "Failed to load posts for page $key. Received code ${response.code()}: ${response.message()}")
                    updateState(RequestState.ERROR_DOWNLOAD)
                    return
                }
                val posts = response.body()
                posts?.let {
                    logInfo(TAG, "Successfully loaded ${it.size} posts for page $key")
                    callback.onResult(it, key+1)
                }?: logWarning(TAG, "Failed to load posts for page $key. Received an empty body")
                updateState(RequestState.SUCCESS)
            }
        })
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Post>) {}

    fun updateState(state: RequestState) {
        this.state.postValue(state)
    }

    companion object {
        private val TAG = PostDataSource::class.java.simpleName
    }
}
