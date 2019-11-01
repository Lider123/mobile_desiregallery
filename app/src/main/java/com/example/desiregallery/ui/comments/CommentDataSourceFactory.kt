package com.example.desiregallery.ui.comments

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.desiregallery.data.models.Comment
import io.reactivex.disposables.CompositeDisposable

/**
 * @author babaetskv on 30.10.19
 */
class CommentDataSourceFactory(
    private val postId: String,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Long, Comment>() {

    val commentDataSourceLiveData: MutableLiveData<CommentDataSource> = MutableLiveData()

    override fun create(): DataSource<Long, Comment> {
        val commentDataSource = CommentDataSource(postId, compositeDisposable)
        commentDataSourceLiveData.postValue(commentDataSource)
        return commentDataSource
    }
}
