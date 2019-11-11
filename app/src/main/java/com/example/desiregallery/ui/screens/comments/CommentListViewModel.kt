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
class CommentListViewModel(
    application: Application,
    postId: String,
    private val baseService: BaseNetworkService
) : AndroidViewModel(application) {
    private val commentDataSourceFactory: CommentDataSourceFactory = CommentDataSourceFactory(postId)

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
        return Transformations.switchMap<CommentDataSource, RequestState>(
            commentDataSourceFactory.commentDataSourceLiveData,
            CommentDataSource::state)
    }

    private fun setState(state: RequestState) {
        commentDataSourceFactory.commentDataSourceLiveData.value?.updateState(state)
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
                commentDataSourceFactory.commentDataSourceLiveData.value?.invalidate()
            }
        })
    }

    companion object {
        private val TAG = CommentListViewModel::class.java.simpleName
        const val PAGE_SIZE = 10
    }
}
