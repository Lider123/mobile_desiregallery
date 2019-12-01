package com.example.desiregallery.ui.screens.feed

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
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

/**
 * @author babaetskv on 31.10.19
 */
class PostsDataSource(private val networkManager: NetworkManager) :
    PageKeyedDataSource<Long, Post>() {
    var state: MutableLiveData<RequestState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, Post>
    ) {
        val query = PostsQueryRequest(params.requestedLoadSize, 0)
        GlobalScope.launch(Dispatchers.Main) {
            updateState(RequestState.DOWNLOADING)
            when (val result = networkManager.getPosts(query)) {
                is Result.Success -> {
                    Timber.i("Successfully got posts")
                    val posts = result.data
                    if (posts.isEmpty()) {
                        Timber.i("There are no posts to download")
                        updateState(RequestState.NO_DATA)
                    } else {
                        Timber.i("Successfully loaded ${posts.size} posts for page 1")

                        val authors: Set<String> =
                            LinkedHashSet(posts.map { post -> post.author.login })
                        when (val usersResult = networkManager.getUsersByNames(authors)) {
                            is Result.Success -> {
                                posts.map { post ->
                                    val users = usersResult.data
                                    val authorName = post.author.login
                                    post.author =
                                        users.find { user -> user.login == authorName } as User
                                }
                            }
                            is Result.Error -> Timber.e(usersResult.exception)
                        }

                        updateState(RequestState.SUCCESS)
                    }
                    callback.onResult(posts, null, 2L)
                }
                is Result.Error -> {
                    Timber.e(result.exception, "Failed to get posts")
                    updateState(RequestState.ERROR_DOWNLOAD)
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Post>) {
        val key = params.key
        val pageSize = params.requestedLoadSize
        val query = PostsQueryRequest(pageSize, pageSize * (key - 1))
        GlobalScope.launch(Dispatchers.Main) {
            updateState(RequestState.DOWNLOADING)
            when (val result = networkManager.getPosts(query)) {
                is Result.Success -> {
                    val posts = result.data
                    Timber.i("Successfully loaded ${posts.size} posts for page $key")

                    val authors: Set<String> =
                        LinkedHashSet(posts.map { post -> post.author.login })
                    when (val usersResult = networkManager.getUsersByNames(authors)) {
                        is Result.Success -> {
                            posts.map { post ->
                                val users = usersResult.data
                                val authorName = post.author.login
                                post.author =
                                    users.find { user -> user.login == authorName } as User
                            }
                        }
                        is Result.Error -> Timber.e(usersResult.exception)
                    }

                    callback.onResult(posts, key + 1)
                    updateState(RequestState.SUCCESS)
                }
                is Result.Error -> updateState(RequestState.ERROR_DOWNLOAD)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Post>) {
    }

    fun updateState(state: RequestState) = this.state.postValue(state)

    class Factory(private val networkManager: NetworkManager) : DataSource.Factory<Long, Post>() {
        val postDataSourceLiveData = MutableLiveData<PostsDataSource>()

        override fun create(): DataSource<Long, Post> = PostsDataSource(networkManager).also {
            postDataSourceLiveData.postValue(it)
        }
    }
}
