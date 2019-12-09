package com.example.desiregallery.ui.screens.post

import android.content.Context
import android.content.Intent
import com.example.desiregallery.data.Result
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.ui.screens.FullScreenImageActivity
import com.example.desiregallery.ui.screens.ImageRateDialog
import com.example.desiregallery.ui.screens.comments.CommentsActivity
import com.example.desiregallery.ui.screens.profile.ProfileActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class PostPresenter(private val networkManager: NetworkManager) : IPostContract.Presenter {
    private lateinit var view: IPostContract.View
    private lateinit var post: Post

    override fun attach(view: IPostContract.View, post: Post) {
        this.view = view
        this.post = post
        with(view) {
            updateImage(post.imageUrl.toString())
            updateRating(post.rating)
            updateAuthorName(post.author.login)
            updateAuthorPhoto(post.author.photo)
            updateTimestamp(post.timestamp)
        }
    }

    override fun onImageClick(context: Context) {
        val imageUrl = post.imageUrl
        imageUrl?.let { goToImageFullScreen(context) }
    }

    override fun onRatingClick(context: Context) {
        val rateDialog = ImageRateDialog(context) { updateRating(it) }
        rateDialog.show()
    }

    override fun onCommentsClick(context: Context) = goToCommentActivity(context)

    override fun onAuthorClick(context: Context) {
        goToProfileActivity(context)
    }

    private fun goToCommentActivity(context: Context) {
        val intent = Intent(context, CommentsActivity::class.java).apply {
            putExtra(CommentsActivity.EXTRA_POST, post)
        }
        context.startActivity(intent)
    }

    private fun goToImageFullScreen(context: Context) {
        val intent = Intent(context, FullScreenImageActivity::class.java).apply {
            putExtra(FullScreenImageActivity.EXTRA_IMAGE_URL, post.imageUrl.toString())
            putExtra(FullScreenImageActivity.EXTRA_POST_ID, post.id)
        }
        context.startActivity(intent)
    }

    private fun goToProfileActivity(context: Context) {
        val intent = Intent(context, ProfileActivity::class.java).apply {
            putExtra(ProfileActivity.EXTRA_USER, post.author)
        }
        context.startActivity(intent)
    }

    private fun updateRating(rate: Float) {
        post.updateRating(rate)
        view.updateRating(post.rating)
        GlobalScope.launch(Dispatchers.Main) {
            when (val result = networkManager.updatePost(post)) {
                is Result.Success -> Timber.i("Post ${post.id} has been successfully updated")
                is Result.Error -> Timber.e(result.exception, "Failed to update post ${post.id}")
            }
        }
    }
}
