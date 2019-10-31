package com.example.desiregallery.ui.comments

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author babaetskv on 30.10.19
 */
class CommentListViewModelFactory(private val application: Application, private val postId: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommentListViewModel(application, postId) as T
    }
}