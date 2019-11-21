package com.example.desiregallery.data.network

import com.example.desiregallery.data.Result
import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.query.requests.CommentsQueryRequest
import com.example.desiregallery.data.network.query.requests.PostsQueryRequest
import timber.log.Timber
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class NetworkManager(
    private val networkService: ApiService,
    private val queryService: QueryService
) : BaseNetworkManager() {

    fun getUsersByNames(userNames: Collection<String>): List<User> {
        Timber.i("Preparing to load ${userNames.size} users")
        val users = ArrayList<User>()
        val executor = Executors.newCachedThreadPool()
        for (name in userNames)
            executor.execute {
                val userResponse = networkService.getUser(name).execute()
                if (!userResponse.isSuccessful || userResponse.body() == null)
                    throw Exception("There was an error while fetching users")

                users.add(userResponse.body()!!)
            }
        executor.shutdown()
        executor.awaitTermination(15, TimeUnit.SECONDS)
        Timber.i("Loaded ${users.size} users")
        if (users.size != userNames.size)
            throw Exception("There was an error while fetching users")
        return users
    }

    suspend fun getUser(login: String): Result<User> = makeSafeCall(
        networkService.getUser(login),
        "Failed to get user $login"
    )

    suspend fun createUser(user: User): Result<User> = makeSafeCall(
        networkService.createUser(user.login, user),
        "Failed to create user"
    )

    suspend fun getComments(query: CommentsQueryRequest): Result<List<Comment>> = makeSafeCall(
        queryService.getComments(query),
        "Failed to get comments"
    )

    suspend fun createComment(comment: Comment): Result<Comment> = makeSafeCall(
        networkService.createComment(comment.id, comment),
        "Failed to create comment"
    )

    suspend fun getPosts(query: PostsQueryRequest): Result<List<Post>> = makeSafeCall(
        queryService.getPosts(query),
        "Failed to get posts"
    )

    suspend fun createPost(post: Post): Result<Post> = makeSafeCall(
        networkService.createPost(post.id, post),
        "Failed to create post"
    )

    suspend fun updatePost(post: Post): Result<Post> = makeSafeCall(
        networkService.updatePost(post.id, post),
        "Failed to update post ${post.id}"
    )

    suspend fun updateUser(user: User): Result<User> = makeSafeCall(
        networkService.updateUser(user.login, user),
        "Failed to update user ${user.login}"
    )
}
