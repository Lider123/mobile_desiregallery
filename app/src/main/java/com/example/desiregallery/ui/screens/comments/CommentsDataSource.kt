package com.example.desiregallery.ui.screens.comments

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.desiregallery.data.Result
import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.data.network.RequestState
import com.example.desiregallery.data.network.query.requests.CommentsQueryRequest
import com.example.desiregallery.ui.screens.base.BaseDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author babaetskv on 30.10.19
 */
class CommentsDataSource(
    private val postId: String,
    private val networkManager: NetworkManager
) : BaseDataSource<Comment>() {

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, Comment>
    ) {
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
                        Timber.i("There are no comments to download")
                        updateState(RequestState.NO_DATA)
                    } else {
                        Timber.i("Successfully loaded ${comments.size} comments for page 1")
                        comments.fetchAuthors()
                        updateState(RequestState.SUCCESS)
                    }
                    callback.onResult(comments, null, 2L)
                }
                is Result.Error -> {
                    Timber.e(result.exception, "Unable to load comments for page 1")
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
                    Timber.i("Successfully loaded ${comments.size} comments for page $key")
                    comments.fetchAuthors()
                    callback.onResult(comments, key + 1)
                    updateState(RequestState.SUCCESS)
                }
                is Result.Error -> {
                    Timber.e(result.exception, "Unable to load comments for page $key")
                    updateState(RequestState.ERROR_DOWNLOAD)
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Comment>) = Unit

    private suspend fun List<Comment>.fetchAuthors() {
        val authors: Set<String> = LinkedHashSet(this.map { comment -> comment.author.login })
        when (val authorsResult = networkManager.getUsersByNames(authors)) {
            is Result.Success -> {
                this.map { comment ->
                    comment.author =
                        authorsResult.data.find { user -> user.login == comment.author.login } as User
                }
            }
            is Result.Error -> Timber.e(authorsResult.exception)
        }
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
