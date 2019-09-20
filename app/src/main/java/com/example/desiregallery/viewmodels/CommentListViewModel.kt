package com.example.desiregallery.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.desiregallery.logging.DGLogger
import com.example.desiregallery.models.Comment
import com.example.desiregallery.network.DGNetwork
import com.example.desiregallery.network.errors.CommentError
import com.example.desiregallery.network.query.OrderDirection
import com.example.desiregallery.network.query.QueryRequest
import com.example.desiregallery.network.query.Value
import com.example.desiregallery.network.query.filters.FieldFilter
import com.example.desiregallery.network.query.operators.ComparisonOperator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author babaetskv on 20.09.19
 */
class CommentListViewModel : ViewModel() {
    val comments = MutableLiveData<List<Comment>>()
    val error = MutableLiveData<CommentError>()

    fun loadComments(postId: String) {
        val commentsQuery = QueryRequest()
            .from("comments")
            .where(FieldFilter("postId", ComparisonOperator.EQUAL, Value(postId)))
            .orderBy("datetime", OrderDirection.ASCENDING)
        DGNetwork.queryService.getComments(commentsQuery).enqueue(object: Callback<List<Comment>> {
            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                DGLogger.logError(TAG, "Unable to load comments: ${t.message}")
                error.value = CommentError.ERROR_DOWNLOAD
            }

            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (!response.isSuccessful) {
                    DGLogger.logInfo(TAG, "Failed to load comments. Response has been received with code ${response.code()}")
                    error.value = CommentError.ERROR_DOWNLOAD
                    return
                }
                val data = response.body()
                data?.let {
                    DGLogger.logInfo(TAG, "Comments have been successfully loaded")
                    if (data.isNotEmpty()) {
                        data as MutableList
                        data.sortBy { comment -> comment.datetime }
                    }
                    comments.value = data
                    error.value = null
                }?: run {
                    DGLogger.logError(TAG, "Failed to load comments. Response has null body")
                    error.value = CommentError.ERROR_DOWNLOAD
                }
            }

        })
    }

    fun addComment(comment: Comment) {
        DGNetwork.baseService.createComment(comment.id, comment).enqueue(object: Callback<Comment> {

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                DGLogger.logError(TAG, "Unable to upload comment: ${t.message}")
                error.value = CommentError.ERROR_UPLOAD
            }

            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                if (!response.isSuccessful) {
                    DGLogger.logError(TAG, "Failed to upload comment. Response has been received with code ${response.code()}")
                    error.value = CommentError.ERROR_UPLOAD
                    return
                }

                val data = comments.value as MutableList
                data.add(comment)
                data.sortBy { comment -> comment.datetime }
                comments.value = data
                error.value = null
            }
        })
    }

    companion object {
        private val TAG = CommentListViewModel::class.java.simpleName
    }
}