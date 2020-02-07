package com.example.desiregallery.ui.screens.feed

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.desiregallery.data.Result
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.data.network.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class PostsViewModel(
    application: Application,
    private val repository: PostsRepository
) : AndroidViewModel(application) {
    var postsLiveData: LiveData<PagedList<Post>> = repository.initData()

    fun getState(): LiveData<RequestState> = repository.state

    fun addPost(post: Post) = repository.addPost(post)

    fun updatePosts() = repository.refreshData()

    class Factory(
        private val application: Application,
        private val repository: PostsRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            PostsViewModel(application, repository) as T
    }
}
