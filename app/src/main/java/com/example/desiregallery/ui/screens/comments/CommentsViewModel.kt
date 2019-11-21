package com.example.desiregallery.ui.screens.comments

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.desiregallery.data.Result
import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.network.*
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author babaetskv on 20.09.19
 */
class CommentsViewModel(
    application: Application,
    postId: String,
    private val networkManager: NetworkManager
) : AndroidViewModel(application) {
    private val commentDataSourceFactory = CommentsDataSource.Factory(postId, networkManager)

    var commentsLiveData: LiveData<PagedList<Comment>>

    init {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .setPrefetchDistance(2)
            .build()
        commentsLiveData = LivePagedListBuilder(commentDataSourceFactory, config)
            .build()
    }

    fun getState(): LiveData<RequestState> {
        return Transformations.switchMap<CommentsDataSource, RequestState>(
            commentDataSourceFactory.commentsDataSourceLiveData,
            CommentsDataSource::state)
    }

    private fun setState(state: RequestState) {
        commentDataSourceFactory.commentsDataSourceLiveData.value?.updateState(state)
    }

    fun addComment(comment: Comment) {
        GlobalScope.launch(Dispatchers.Main) {
            when (val result = networkManager.createComment(comment)) {
                is Result.Success -> {
                    logInfo(TAG, "Comment ${comment.id} has been successfully created")
                    setState(RequestState.SUCCESS)
                    commentDataSourceFactory.commentsDataSourceLiveData.value?.invalidate()
                }
                is Result.Error -> {
                    logError(TAG, result.exception.message ?: "Failed to create comment")
                    setState(RequestState.ERROR_UPLOAD)
                }
            }
        }
    }

    companion object {
        private val TAG = CommentsViewModel::class.java.simpleName
        const val PAGE_SIZE = 10
    }

    class Factory(
        private val postId: String,
        private val application: Application,
        private val networkManager: NetworkManager
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CommentsViewModel(application, postId, networkManager) as T
        }
    }
}
