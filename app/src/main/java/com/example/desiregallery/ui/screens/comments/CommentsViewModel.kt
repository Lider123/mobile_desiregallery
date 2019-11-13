package com.example.desiregallery.ui.screens.comments

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author babaetskv on 20.09.19
 */
class CommentsViewModel(
    application: Application,
    postId: String,
    private val baseService: BaseNetworkService,
    queryService: QueryNetworkService,
    networkUtils: NetworkUtils
) : AndroidViewModel(application) {
    private val commentDataSourceFactory = CommentsDataSource.Factory(postId, queryService, networkUtils)

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
        baseService.createComment(comment.id, comment).enqueue(object: Callback<Comment> {

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                logError(TAG, "Unable to upload comment: ${t.message}")
                setState(RequestState.ERROR_UPLOAD)
            }

            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                if (!response.isSuccessful) {
                    logError(TAG, "Failed to upload comment. Response has been received with code ${response.code()}")
                    setState(RequestState.ERROR_UPLOAD)
                    return
                }
                logInfo(TAG, "Comment successfully uploaded")

                setState(RequestState.SUCCESS)
                commentDataSourceFactory.commentsDataSourceLiveData.value?.invalidate()
            }
        })
    }

    companion object {
        private val TAG = CommentsViewModel::class.java.simpleName
        const val PAGE_SIZE = 10
    }

    class Factory(
        private val postId: String,
        private val application: Application,
        private val baseService: BaseNetworkService,
        private val queryService: QueryNetworkService,
        private val networkUtils: NetworkUtils
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CommentsViewModel(application, postId, baseService, queryService, networkUtils) as T
        }
    }
}
