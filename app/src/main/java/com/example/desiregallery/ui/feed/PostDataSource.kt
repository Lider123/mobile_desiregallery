package com.example.desiregallery.ui.feed

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.network.QueryNetworkService
import com.example.desiregallery.data.network.RequestState
import com.example.desiregallery.data.network.query.OrderDirection
import com.example.desiregallery.data.network.query.QueryRequest
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
import com.example.desiregallery.utils.logWarning
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * @author babaetskv on 31.10.19
 */
class PostDataSource(
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Long, Post>(), KoinComponent {
    private val networkService: QueryNetworkService by inject()

    var state: MutableLiveData<RequestState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Post>) {
        updateState(RequestState.DOWNLOADING)
        val query = QueryRequest()
            .from("posts")
            .limit(params.requestedLoadSize)
            .orderBy("timestamp", OrderDirection.DESCENDING)
        compositeDisposable.add(
            networkService.getPosts(query).subscribe(
                { posts ->
                    logInfo(TAG, "Posts for page 1 have been loaded")
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
                },
                { error ->
                    logError(TAG, "Unable to load posts for page 1: ${error.message}")
                    updateState(RequestState.ERROR_DOWNLOAD)
                }
            )
        )
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Post>) {
        val key = params.key
        val pageSize = params.requestedLoadSize
        updateState(RequestState.DOWNLOADING)
        val query = QueryRequest()
            .from("posts")
            .limit(pageSize)
            .orderBy("timestamp", OrderDirection.DESCENDING)
            .offset(pageSize * (key-1))
        compositeDisposable.add(
            networkService.getPosts(query).subscribe(
                { posts ->
                    posts?.let {
                        logInfo(TAG, "Successfully loaded ${it.size} posts for page $key")
                        callback.onResult(it, key+1)
                    }?: logWarning(TAG, "Failed to load posts for page ${key}. Received an empty body")
                    updateState(RequestState.SUCCESS)
                },
                { error ->
                    logError(TAG, "Unable to load posts for page ${key}: ${error.message}")
                    updateState(RequestState.ERROR_DOWNLOAD)
                }
            )
        )
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Post>) {}

    fun updateState(state: RequestState) {
        this.state.postValue(state)
    }

    companion object {
        private val TAG = PostDataSource::class.java.simpleName
    }
}
