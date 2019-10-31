package com.example.desiregallery.ui.feed

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.network.IStatusHandler

/**
 * @author babaetskv on 31.10.19
 */
class PostDataSourceFactory(
    private val statusHandler: IStatusHandler
) : DataSource.Factory<Long, Post>() {
    private lateinit var postDataSource: PostDataSource

    val mutableLiveData: MutableLiveData<PostDataSource> = MutableLiveData()

    override fun create(): DataSource<Long, Post> {
        postDataSource = PostDataSource(statusHandler)
        mutableLiveData.postValue(postDataSource)
        return postDataSource
    }
}
