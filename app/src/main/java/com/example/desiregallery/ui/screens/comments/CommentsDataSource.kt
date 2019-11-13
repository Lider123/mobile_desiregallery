package com.example.desiregallery.ui.screens.comments

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
import com.example.desiregallery.utils.logWarning
import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.NetworkUtils
import com.example.desiregallery.data.network.QueryNetworkService
import com.example.desiregallery.data.network.RequestState
import com.example.desiregallery.data.network.query.requests.CommentsQueryRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author babaetskv on 30.10.19
 */
class CommentsDataSource(
    private val postId: String,
    private val networkService: QueryNetworkService,
    private val networkUtils: NetworkUtils
) : PageKeyedDataSource<Long, Comment>() {
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

                        val authors: Set<String> = LinkedHashSet(comments.map { comment -> comment.author.login })
                        val users = networkUtils.getUsersByNames(authors)
                        comments.map { comment ->
                            val authorName = comment.author.login
                            comment.author = users.find { user -> user.login == authorName } as User
                        }

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

                    val authors: Set<String> = LinkedHashSet(comments.map { comment -> comment.author.login })
                    val users = networkUtils.getUsersByNames(authors)
                    comments.map { comment ->
                        val authorName = comment.author.login
                        comment.author = users.find { user -> user.login == authorName } as User
                    }

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
        private val TAG = CommentsDataSource::class.java.simpleName
    }

    class Factory(
        private val postId: String,
        private val networkService: QueryNetworkService,
        private val networkUtils: NetworkUtils
    ) : DataSource.Factory<Long, Comment>() {

        val commentsDataSourceLiveData: MutableLiveData<CommentsDataSource> = MutableLiveData()

        override fun create(): DataSource<Long, Comment> {
            val commentDataSource = CommentsDataSource(postId, networkService, networkUtils)
            commentsDataSourceLiveData.postValue(commentDataSource)
            return commentDataSource
        }
    }
}
