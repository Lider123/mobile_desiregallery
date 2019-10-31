package com.example.desiregallery.ui.feed

import androidx.paging.PageKeyedDataSource
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.network.IStatusHandler
import com.example.desiregallery.data.network.RequestStatus
import com.example.desiregallery.data.network.query.OrderDirection
import com.example.desiregallery.data.network.query.QueryRequest
import com.example.desiregallery.data.network.queryService
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
import com.example.desiregallery.utils.logWarning
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author babaetskv on 31.10.19
 */
class PostDataSource(private val statusHandler: IStatusHandler) : PageKeyedDataSource<Long, Post>() {

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Post>) {
        statusHandler.setRequestStatus(RequestStatus.DOWNLOADING)
        val query = QueryRequest()
            .from("posts")
            .limit(params.requestedLoadSize)
            .orderBy("timestamp", OrderDirection.DESCENDING)
        queryService.getPosts(query).enqueue(object: Callback<List<Post>> {

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    logWarning(TAG, "Failed to load posts for page 1. Response received with code ${response.code()}: ${response.message()}")
                    statusHandler.setRequestStatus(RequestStatus.ERROR_DOWNLOAD)
                    return
                }
                logInfo(TAG, "Posts for page 1 have been loaded")

                val posts = response.body()
                posts?.let {
                    if (posts.isEmpty()) {
                        logInfo(TAG, "There are no posts to download")
                        statusHandler.setRequestStatus(RequestStatus.NO_DATA)
                    }
                    else {
                        logInfo(TAG, "Successfully loaded ${it.size} posts for page 1")
                        statusHandler.setRequestStatus(RequestStatus.SUCCESS)
                    }
                    callback.onResult(it, null, 2L)
                }?: logWarning(TAG, "Failed to load posts for page 1. Received an empty body")
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                logError(TAG, "Unable to load posts for page 1: ${t.message}")
                statusHandler.setRequestStatus(RequestStatus.ERROR_DOWNLOAD)
            }
        })
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Post>) {
        val key = params.key
        val pageSize = params.requestedLoadSize
        statusHandler.setRequestStatus(RequestStatus.DOWNLOADING)
        val query = QueryRequest()
            .from("posts")
            .limit(pageSize)
            .orderBy("timestamp", OrderDirection.DESCENDING)
            .offset(pageSize * (key-1))
        queryService.getPosts(query).enqueue(object: Callback<List<Post>> {

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    logWarning(TAG, "Failed to load posts for page ${key}. Response received with code ${response.code()}: ${response.message()}")
                    statusHandler.setRequestStatus(RequestStatus.ERROR_DOWNLOAD)
                    return
                }

                val posts = response.body()
                posts?.let {
                    logInfo(TAG, "Successfully loaded ${it.size} posts for page $key")
                    callback.onResult(it, key+1)
                }?: logWarning(TAG, "Failed to load posts for page ${key}. Received an empty body")
                statusHandler.setRequestStatus(RequestStatus.SUCCESS)
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                logError(TAG, "Unable to load posts for page ${key}: ${t.message}")
                statusHandler.setRequestStatus(RequestStatus.ERROR_DOWNLOAD)
            }
        })
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Post>) {}

    companion object {
        private val TAG = PostDataSource::class.java.simpleName
    }
}
