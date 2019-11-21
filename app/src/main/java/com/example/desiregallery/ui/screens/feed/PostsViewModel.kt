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
    private val networkManager: NetworkManager
) : AndroidViewModel(application) {
    private val postDataSourceFactory = PostsDataSource.Factory(networkManager)

    var postsLiveData: LiveData<PagedList<Post>>

    init {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .setPrefetchDistance(3)
            .build()
        postsLiveData = LivePagedListBuilder(postDataSourceFactory, config).build()
    }

    private fun setState(state: RequestState) {
        postDataSourceFactory.postDataSourceLiveData.value?.updateState(state)
    }

    fun getState(): LiveData<RequestState> {
        return Transformations.switchMap<PostsDataSource, RequestState>(
            postDataSourceFactory.postDataSourceLiveData,
            PostsDataSource::state)
    }

    fun addPost(post: Post) {
        post.timestamp = Date().time
        GlobalScope.launch(Dispatchers.Main) {
            when (val result = networkManager.createPost(post)) {
                is Result.Success -> {
                    Timber.i("Post ${post.id} has been successfully created")
                    setState(RequestState.SUCCESS)
                    postDataSourceFactory.postDataSourceLiveData.value?.invalidate()
                }
                is Result.Error -> {
                    Timber.e(result.exception, "Failed to create post")
                    setState(RequestState.ERROR_UPLOAD)
                }
            }
        }
    }

    fun updatePosts() = postDataSourceFactory.postDataSourceLiveData.value?.invalidate()

    companion object {
        private const val PAGE_SIZE = 10
    }

    class Factory(
        private val application: Application,
        private val networkManager: NetworkManager
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostsViewModel(application, networkManager) as T
        }

    }
}
