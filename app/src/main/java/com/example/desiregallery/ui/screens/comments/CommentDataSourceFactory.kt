package com.example.desiregallery.ui.screens.comments

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.desiregallery.data.models.Comment

/**
 * @author babaetskv on 30.10.19
 */
class CommentDataSourceFactory(private val postId: String) : DataSource.Factory<Long, Comment>() {

    val commentDataSourceLiveData: MutableLiveData<CommentDataSource> = MutableLiveData()

    override fun create(): DataSource<Long, Comment> {
        val commentDataSource = CommentDataSource(postId)
        commentDataSourceLiveData.postValue(commentDataSource)
        return commentDataSource
    }
}
