package com.example.desiregallery.ui.screens.feed

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author Konstantin on 12.11.2019
 */
class PostListViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PostListViewModel(application) as T
    }

}