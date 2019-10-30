package com.example.desiregallery.datasources

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.desiregallery.models.Comment
import com.example.desiregallery.network.IStatusHandler

/**
 * @author babaetskv on 30.10.19
 */
class CommentDataSourceFactory(private val postId: String, private val statusHandler: IStatusHandler) : DataSource.Factory<Long, Comment>() {
    private lateinit var commentDataSource: CommentDataSource

    val mutableLiveData: MutableLiveData<CommentDataSource> = MutableLiveData()

    override fun create(): DataSource<Long, Comment> {
        commentDataSource = CommentDataSource(postId, statusHandler)
        mutableLiveData.postValue(commentDataSource)
        return commentDataSource
    }
}
