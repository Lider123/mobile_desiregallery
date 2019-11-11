package com.example.desiregallery.ui.screens.feed

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.desiregallery.data.models.Post

/**
 * @author babaetskv on 31.10.19
 */
class PostDataSourceFactory : DataSource.Factory<Long, Post>() {
    val postDataSourceLiveData = MutableLiveData<PostDataSource>()

    override fun create(): DataSource<Long, Post> {
        val postDataSource = PostDataSource()
        postDataSourceLiveData.postValue(postDataSource)
        return postDataSource
    }
}
