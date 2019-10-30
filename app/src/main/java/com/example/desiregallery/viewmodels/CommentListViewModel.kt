package com.example.desiregallery.viewmodels

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.desiregallery.datasources.CommentDataSource
import com.example.desiregallery.datasources.CommentDataSourceFactory
import com.example.desiregallery.logging.logError
import com.example.desiregallery.logging.logInfo
import com.example.desiregallery.models.Comment
import com.example.desiregallery.network.IStatusHandler
import com.example.desiregallery.network.baseService
import com.example.desiregallery.network.RequestStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * @author babaetskv on 20.09.19
 */
class CommentListViewModel(application: Application, postId: String) : AndroidViewModel(application), IStatusHandler {
    private val commentDataSourceFactory: CommentDataSourceFactory = CommentDataSourceFactory(postId, this)
    private val commentDataSourceLiveData: MutableLiveData<CommentDataSource>
    private val executor: Executor
    val pagedListLiveData: LiveData<PagedList<Comment>>
    var requestStatus = MutableLiveData<RequestStatus>()

    init {
        commentDataSourceLiveData = commentDataSourceFactory.mutableLiveData

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .setPrefetchDistance(2)
            .build()
        executor = Executors.newFixedThreadPool(5)
        pagedListLiveData = LivePagedListBuilder(commentDataSourceFactory, config)
            .setFetchExecutor(executor)
            .build()
    }

    override fun setRequestStatus(status: RequestStatus) {
        requestStatus.postValue(status)
    }

    /*fun loadComments(postId: String) {
        val commentsQuery = QueryRequest()
            .from("comments")
            .where(FieldFilter("postId", ComparisonOperator.EQUAL, Value(postId)))
            .orderBy("timestamp", OrderDirection.ASCENDING)
        queryService.getComments(commentsQuery).enqueue(object: Callback<List<Comment>> {
            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                logError(TAG, "Unable to load comments: ${t.message}")
                error.value = RequestStatus.ERROR_DOWNLOAD
            }

            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (!response.isSuccessful) {
                    logInfo(TAG, "Failed to load comments. Response has been received with code ${response.code()}")
                    error.value = RequestStatus.ERROR_DOWNLOAD
                    return
                }
                val data = response.body()
                data?.let {
                    logInfo(TAG, "Comments have been successfully loaded")
                    if (data.isNotEmpty()) {
                        data as MutableList
                        data.sortBy { comment -> comment.timestamp }
                    }
                    comments.value = data
                    error.value = null
                }?: run {
                    logError(TAG, "Failed to load comments. Response has null body")
                    error.value = RequestStatus.ERROR_DOWNLOAD
                }
            }

        })
    }*/

    fun addComment(comment: Comment) {
        baseService.createComment(comment.id, comment).enqueue(object: Callback<Comment> {

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                logError(TAG, "Unable to upload comment: ${t.message}")
                setRequestStatus(RequestStatus.ERROR_UPLOAD)
            }

            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                if (!response.isSuccessful) {
                    logError(TAG, "Failed to upload comment. Response has been received with code ${response.code()}")
                    setRequestStatus(RequestStatus.ERROR_UPLOAD)
                    return
                }
                logInfo(TAG, "Comment successfully uploaded")

                setRequestStatus(RequestStatus.SUCCESS)
                commentDataSourceLiveData.value?.invalidate()
            }
        })
    }

    companion object {
        private val TAG = CommentListViewModel::class.java.simpleName

        const val PAGE_SIZE = 10
    }
}