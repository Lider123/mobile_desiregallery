package com.example.desiregallery.data.network

import com.example.desiregallery.data.Result
import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.models.Notification
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.query.requests.CommentsQueryRequest
import com.example.desiregallery.data.network.query.requests.PostsQueryRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class NetworkManager(
    private val networkService: ApiService,
    private val queryService: QueryService
) : BaseNetworkManager() {

    suspend fun getUsersByNames(userNames: Collection<String>): Result<List<User>> =
        withContext(Dispatchers.IO) {
            Timber.i("Preparing to load ${userNames.size} users")
            val users = ArrayList<User>()
            try {
                val executor = Executors.newCachedThreadPool()
                for (name in userNames)
                    executor.execute {
                        val userResponse = networkService.getUser(name).execute()
                        val user = userResponse.body()
                        if (!userResponse.isSuccessful || user == null) throw IOException("There was an error while fetching users")

                        users.add(user)
                    }
                executor.shutdown()
                executor.awaitTermination(15, TimeUnit.SECONDS)
                Timber.i("Loaded ${users.size} users")
                if (users.size != userNames.size)
                    throw IOException("There was an error while fetching users")
            } catch (e: IOException) {
                return@withContext Result.Error(e)
            }
            return@withContext Result.Success(users)
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

    suspend fun updateNotification(notification: Notification): Result<Notification> = makeSafeCall(
        networkService.updateNotification(notification.loginTo, notification),
        "Failed to update notification for user ${notification.loginTo}"
    )
}
