package com.example.desiregallery.ui.screens.comments

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author Konstantin on 11.11.2019
 */
class CommentListViewModelFactory(
    private val application: Application,
    private val postId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CommentListViewModel(application, postId) as T
    }

}