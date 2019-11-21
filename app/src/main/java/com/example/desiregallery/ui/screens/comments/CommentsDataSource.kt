package com.example.desiregallery.ui.screens.comments

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.example.desiregallery.data.Result
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.data.network.RequestState
import com.example.desiregallery.data.network.query.requests.CommentsQueryRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author babaetskv on 30.10.19
 */
class CommentsDataSource(
    private val postId: String,
    private val networkManager: NetworkManager
) : PageKeyedDataSource<Long, Comment>() {
    var state: MutableLiveData<RequestState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Long>,
                             callback: LoadInitialCallback<Long, Comment>) {
        val query = CommentsQueryRequest(
            postId,
            params.requestedLoadSize,
            0
        )
        GlobalScope.launch(Dispatchers.Main) {
            updateState(RequestState.DOWNLOADING)
            when (val result = networkManager.getComments(query)) {
                is Result.Success -> {
                    val comments = result.data
                    if (comments.isEmpty()) {
                        logInfo(TAG, "There are no comments to download")
                        updateState(RequestState.NO_DATA)
                    }
                    else {
                        logInfo(TAG, "Successfully loaded ${comments.size} comments for page 1")

                        val authors: Set<String> = LinkedHashSet(comments.map { comment -> comment.author.login })
                        val users = networkManager.getUsersByNames(authors)
                        comments.map { comment ->
                            val authorName = comment.author.login
                            comment.author = users.find { user -> user.login == authorName } as User
                        }

                        updateState(RequestState.SUCCESS)
                    }
                    callback.onResult(comments, null, 2L)
                }
                is Result.Error -> {
                    logError(TAG, "Unable to load comments for page 1: ${result.exception.message}")
                    updateState(RequestState.ERROR_DOWNLOAD)
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Comment>) {
        val key = params.key
        val pageSize = params.requestedLoadSize
        val query = CommentsQueryRequest(
            postId,
            pageSize,
            pageSize * (key - 1)
        )
        GlobalScope.launch(Dispatchers.Main) {
            updateState(RequestState.DOWNLOADING)
            when (val result = networkManager.getComments(query)) {
                is Result.Success -> {
                    val comments = result.data
                    logInfo(TAG, "Successfully loaded ${comments.size} comments for page $key")

                    val authors: Set<String> = LinkedHashSet(comments.map { comment -> comment.author.login })
                    val users = networkManager.getUsersByNames(authors)
                    comments.map { comment ->
                        val authorName = comment.author.login
                        comment.author = users.find { user -> user.login == authorName } as User
                    }

                    callback.onResult(comments, key+1)
                    updateState(RequestState.SUCCESS)
                }
                is Result.Error -> {
                    logError(TAG, "Unable to load comments for page $key: ${result.exception.message}")
                    updateState(RequestState.ERROR_DOWNLOAD)
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Comment>) {}

    fun updateState(state: RequestState) = this.state.postValue(state)

    companion object {
        private val TAG = CommentsDataSource::class.java.simpleName
    }

    class Factory(
        private val postId: String,
        private val networkManager: NetworkManager
    ) : DataSource.Factory<Long, Comment>() {
        val commentsDataSourceLiveData: MutableLiveData<CommentsDataSource> = MutableLiveData()

        override fun create(): DataSource<Long, Comment> {
            val commentDataSource = CommentsDataSource(postId, networkManager)
            commentsDataSourceLiveData.postValue(commentDataSource)
            return commentDataSource
        }
    }
}
