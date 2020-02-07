package com.example.desiregallery.ui.screens.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
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

/**
 * @author babaetskv on 07.02.20
 */
class PostsRepository(private val networkManager: NetworkManager) {
    private val postDataSourceFactory = PostsDataSource.Factory(networkManager)
    val state: LiveData<RequestState>
        get() = Transformations.switchMap<PostsDataSource, RequestState>(
            postDataSourceFactory.postDataSourceLiveData,
            PostsDataSource::state
        )

    private fun setState(state: RequestState) {
        postDataSourceFactory.postDataSourceLiveData.value?.updateState(state)
    }

    fun initData(): LiveData<PagedList<Post>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .setPrefetchDistance(3)
            .build()
        return LivePagedListBuilder(postDataSourceFactory, config).build()
    }

    fun refreshData() = postDataSourceFactory.postDataSourceLiveData.value?.invalidate()

    fun addPost(post: Post) {
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

    companion object {
        private const val PAGE_SIZE = 10
    }
}
