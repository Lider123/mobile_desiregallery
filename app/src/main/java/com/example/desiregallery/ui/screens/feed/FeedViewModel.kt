package com.example.desiregallery.ui.screens.feed

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.desiregallery.data.Result
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.data.network.RequestState
import com.example.desiregallery.data.network.query.requests.PostsQueryRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.StringBuilder
import java.util.*
import kotlin.math.min

class FeedViewModel(
    application: Application,
    private val networkManager: NetworkManager
) : AndroidViewModel(application) {
    private val postDataSourceFactory = PostsDataSource.Factory(networkManager)

    var postsLiveData: LiveData<PagedList<Post>>
    val usersLiveData = MutableLiveData<List<User>>()

    init {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .setPrefetchDistance(3)
            .build()
        postsLiveData = LivePagedListBuilder(postDataSourceFactory, config).build()
        loadRatedUsers()
    }

    private fun setState(state: RequestState) {
        postDataSourceFactory.postDataSourceLiveData.value?.updateState(state)
    }

    private fun loadRatedUsers() {
        GlobalScope.launch(Dispatchers.Main) {
            val query = PostsQueryRequest(null, 0)
            when (val result = networkManager.getPosts(query)) {
                is Result.Success -> {
                    Timber.i("Successfully loaded rated posts")
                    val authors = result.data
                        .groupingBy { post -> post.author.login }
                        .aggregate { _, accumulator: StringBuilder?, element, first ->
                            if (first) StringBuilder().append(element.rating).append(" ")
                            else accumulator!!.append(element.rating).append(" ")
                        }
                        .mapValues {
                            it.value.toString()
                                .trim()
                                .split(" ")
                                .map(String::toFloat)
                                .average()
                                .toFloat()
                        }
                        .toList()
                        .sortedByDescending { (_, value) -> value }
                        .map {
                            it.first
                        }
                        .let {
                            val size = min(it.size, 5)
                            it.slice(IntRange(0, size - 1))
                        }.let {
                            fetchAuthors(it)
                        }
                    usersLiveData.postValue(authors)
                }
                is Result.Error -> Timber.e("Failed to get rated posts")
            }
        }
    }

    private suspend fun fetchAuthors(authors: List<String>): List<User> {
        return when (val usersResult = networkManager.getUsersByNames(authors)) {
            is Result.Success -> usersResult.data
            is Result.Error -> {
                Timber.e(usersResult.exception)
                emptyList()
            }
        }
    }

    fun getState(): LiveData<RequestState> =
        Transformations.switchMap<PostsDataSource, RequestState>(
            postDataSourceFactory.postDataSourceLiveData,
            PostsDataSource::state
        )

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

    fun updatePosts() = postDataSourceFactory.postDataSourceLiveData.value?.invalidate()

    companion object {
        private const val PAGE_SIZE = 10
    }

    class Factory(
        private val application: Application,
        private val networkManager: NetworkManager
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            FeedViewModel(application, networkManager) as T
    }
}
