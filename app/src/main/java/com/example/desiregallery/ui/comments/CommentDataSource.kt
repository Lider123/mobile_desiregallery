package com.example.desiregallery.ui.comments

import androidx.paging.PageKeyedDataSource
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
import com.example.desiregallery.utils.logWarning
import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.network.IStatusHandler
import com.example.desiregallery.data.network.RequestStatus
import com.example.desiregallery.data.network.query.OrderDirection
import com.example.desiregallery.data.network.query.QueryRequest
import com.example.desiregallery.data.network.query.Value
import com.example.desiregallery.data.network.query.filters.FieldFilter
import com.example.desiregallery.data.network.query.operators.ComparisonOperator
import com.example.desiregallery.data.network.queryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author babaetskv on 30.10.19
 */
class CommentDataSource(
    private val postId: String,
    private val statusHandler: IStatusHandler
) : PageKeyedDataSource<Long, Comment>() {

    override fun loadInitial(params: LoadInitialParams<Long>,
                             callback: LoadInitialCallback<Long, Comment>) {
        statusHandler.setRequestStatus(RequestStatus.DOWNLOADING)
        val query = QueryRequest()
            .from("comments")
            .where(FieldFilter("postId", ComparisonOperator.EQUAL, Value(postId)))
            .limit(params.requestedLoadSize)
            .orderBy("timestamp", OrderDirection.ASCENDING)
        queryService.getComments(query).enqueue(object: Callback<List<Comment>> {

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                logError(
                    TAG,
                    "Unable to load comments for page 1: ${t.message}"
                )
                statusHandler.setRequestStatus(RequestStatus.ERROR_DOWNLOAD)
            }

            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (!response.isSuccessful) {
                    logWarning(TAG, "Failed to load comments for page 1. Response has been received with code ${response.code()}")
                    statusHandler.setRequestStatus(RequestStatus.ERROR_DOWNLOAD)
                    return
                }
                logInfo(TAG, "Successfully loaded comments for page 1")

                val comments = response.body()
                comments?.let {
                    if (comments.isEmpty()) {
                        logInfo(TAG, "There are no comments to download")
                        statusHandler.setRequestStatus(RequestStatus.NO_DATA)
                    }
                    else {
                        logInfo(TAG, "Successfully loaded ${it.size} comments for page 1")
                        statusHandler.setRequestStatus(RequestStatus.SUCCESS)
                    }
                    callback.onResult(it, null, 2L)
                }?: logWarning(TAG, "Failed to load comments for page 1. Received an empty body")
            }
        })
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Comment>) {
        statusHandler.setRequestStatus(RequestStatus.DOWNLOADING)
        val query = QueryRequest()
            .from("comments")
            .where(FieldFilter("postId", ComparisonOperator.EQUAL, Value(postId)))
            .limit(params.requestedLoadSize)
            .orderBy("timestamp", OrderDirection.ASCENDING)
            .offset(params.requestedLoadSize * (params.key-1))
        queryService.getComments(query).enqueue(object: Callback<List<Comment>> {

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                logError(TAG, "Unable to load comments for page ${params.key}: ${t.message}")
                statusHandler.setRequestStatus(RequestStatus.ERROR_DOWNLOAD)
            }

            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (!response.isSuccessful) {
                    logWarning(TAG, "Failed to load comments for page ${params.key}. Response has been received with code ${response.code()}")
                    statusHandler.setRequestStatus(RequestStatus.ERROR_DOWNLOAD)
                    return
                }

                val comments = response.body()
                comments?.let {
                    logInfo(TAG, "Successfully loaded ${it.size} comments for page ${params.key}")
                    callback.onResult(it, params.key+1)
                }?: logWarning(TAG, "Failed to load comments for page ${params.key}. Received an empty body")
                statusHandler.setRequestStatus(RequestStatus.SUCCESS)
            }
        })
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Comment>) {}

    companion object {
        private val TAG = CommentDataSource::class.java.simpleName
    }
}
