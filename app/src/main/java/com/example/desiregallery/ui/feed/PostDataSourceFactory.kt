package com.example.desiregallery.ui.feed

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.desiregallery.data.models.Post
import io.reactivex.disposables.CompositeDisposable

/**
 * @author babaetskv on 31.10.19
 */
class PostDataSourceFactory(
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Long, Post>() {
    val postDataSourceLiveData = MutableLiveData<PostDataSource>()

    override fun create(): DataSource<Long, Post> {
        val postDataSource = PostDataSource(compositeDisposable)
        postDataSourceLiveData.postValue(postDataSource)
        return postDataSource
    }
}
