package com.example.desiregallery.ui.comments

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
import com.example.desiregallery.utils.logWarning
import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.network.QueryNetworkService
import com.example.desiregallery.data.network.RequestState
import com.example.desiregallery.data.network.query.requests.CommentsQueryRequest
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * @author babaetskv on 30.10.19
 */
class CommentDataSource(
    private val postId: String,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Long, Comment>(), KoinComponent {
    private val networkService: QueryNetworkService by inject()

    var state: MutableLiveData<RequestState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Long>,
                             callback: LoadInitialCallback<Long, Comment>) {
        updateState(RequestState.DOWNLOADING)
        val query = CommentsQueryRequest(
            postId,
            params.requestedLoadSize,
            0
        )
        compositeDisposable.add(
            networkService.getComments(query).subscribe(
                { comments ->
                    logInfo(TAG, "Successfully loaded comments for page 1")
                    comments?.let {
                        if (comments.isEmpty()) {
                            logInfo(TAG, "There are no comments to download")
                            updateState(RequestState.NO_DATA)
                        }
                        else {
                            logInfo(TAG, "Successfully loaded ${it.size} comments for page 1")
                            updateState(RequestState.SUCCESS)
                        }
                        callback.onResult(it, null, 2L)
                    }?: logWarning(TAG, "Failed to load comments for page 1. Received an empty body")
                },
                { error ->
                    logError(TAG, "Unable to load comments for page 1: ${error.message}")
                    updateState(RequestState.ERROR_DOWNLOAD)
                }
            )
        )
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Comment>) {
        val key = params.key
        val pageSize = params.requestedLoadSize
        updateState(RequestState.DOWNLOADING)
        val query = CommentsQueryRequest(
            postId,
            pageSize,
            pageSize * (key - 1)
        )
        compositeDisposable.add(
            networkService.getComments(query).subscribe(
                { comments ->
                    comments?.let {
                        logInfo(TAG, "Successfully loaded ${it.size} comments for page $key")
                        callback.onResult(it, key+1)
                    }?: logWarning(TAG, "Failed to load comments for page ${key}. Received an empty body")
                    updateState(RequestState.SUCCESS)
                },
                { error ->
                    logError(TAG, "Unable to load comments for page ${key}: ${error.message}")
                    updateState(RequestState.ERROR_DOWNLOAD)
                }
            )
        )
    }

    fun updateState(state: RequestState) {
        this.state.postValue(state)
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Comment>) {}

    companion object {
        private val TAG = CommentDataSource::class.java.simpleName
    }
}
