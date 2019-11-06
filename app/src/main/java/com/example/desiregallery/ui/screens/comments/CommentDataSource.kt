package com.example.desiregallery.ui.screens.comments

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
import com.example.desiregallery.utils.logWarning
import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.network.QueryNetworkService
import com.example.desiregallery.data.network.RequestState
import com.example.desiregallery.data.network.query.requests.CommentsQueryRequest
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author babaetskv on 30.10.19
 */
class CommentDataSource(
    private val postId: String
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
        networkService.getComments(query).enqueue(object: Callback<List<Comment>> {

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                logError(TAG, "Unable to load comments for page 1: ${t.message}")
                updateState(RequestState.ERROR_DOWNLOAD)
            }

            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (!response.isSuccessful) {
                    logWarning(TAG, "Failed to load comments for page 1. Received code ${response.code()}: ${response.message()}")
                    updateState(RequestState.ERROR_DOWNLOAD)
                    return
                }
                val comments = response.body()
                comments?.let {
                    if (it.isEmpty()) {
                        logInfo(TAG, "There are no comments to download")
                        updateState(RequestState.NO_DATA)
                    }
                    else {
                        logInfo(TAG, "Successfully loaded ${it.size} comments for page 1")
                        updateState(RequestState.SUCCESS)
                    }
                    callback.onResult(it, null, 2L)
                }?: logWarning(TAG, "Failed to load comments for page 1. Received an empty body")
            }
        })
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

        networkService.getComments(query).enqueue(object: Callback<List<Comment>> {

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                logError(TAG, "Unable to load comments for page $key: ${t.message}")
                updateState(RequestState.ERROR_DOWNLOAD)
            }

            override fun onResponse(
                call: Call<List<Comment>>,
                response: Response<List<Comment>>
            ) {
                if (!response.isSuccessful) {
                    logWarning(TAG, "Failed to load comments for page $key. Received code ${response.code()}: ${response.message()}")
                    updateState(RequestState.ERROR_DOWNLOAD)
                    return
                }
                val comments = response.body()
                comments?.let {
                    logInfo(TAG, "Successfully loaded ${it.size} comments for page $key")
                    callback.onResult(it, key+1)
                }?: logWarning(TAG, "Failed to load comments for page $key. Received an empty body")
                updateState(RequestState.SUCCESS)
            }
        })
    }

    fun updateState(state: RequestState) {
        this.state.postValue(state)
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Comment>) {}

    companion object {
        private val TAG = CommentDataSource::class.java.simpleName
    }
}
