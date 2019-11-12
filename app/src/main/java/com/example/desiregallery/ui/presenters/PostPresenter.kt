package com.example.desiregallery.ui.presenters

import android.content.Context
import android.content.Intent
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.network.BaseNetworkService
import com.example.desiregallery.ui.screens.comments.CommentsActivity
import com.example.desiregallery.ui.dialogs.ImageRateDialog
import com.example.desiregallery.ui.screens.FullScreenImageActivity
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostPresenter(
    private val networkService: BaseNetworkService
): IPostContract.Presenter {
    private lateinit var view: IPostContract.View
    private lateinit var post: Post

    override fun attach(view: IPostContract.View, post: Post) {
        this.view = view
        this.post = post
        view.updateImage(post.imageUrl.toString())
        view.updateRating(post.rating)
        view.updateAuthorName(post.author.login)
        view.updateAuthorPhoto(post.author.photo)
        view.updateTimestamp(post.timestamp)
    }

    override fun onImageClick(context: Context) {
        val imageUrl = post.imageUrl
        imageUrl?.let { goToImageFullScreen(context) }
    }

    override fun onRatingClick(context: Context) {
        val rateDialog = ImageRateDialog(context) { rate -> updateRating(rate) }
        rateDialog.show()
    }

    override fun onCommentsClick(context: Context) {
        goToCommentActivity(context)
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

    private fun updateRating(rate: Float) {
        post.updateRating(rate)
        view.updateRating(post.rating)
        networkService.updatePost(post.id, post).enqueue(object: Callback<Post> {

            override fun onFailure(call: Call<Post>, t: Throwable) {
                logError(TAG, "Unable to update post ${post.id}: ${t.message}")
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful)
                    logInfo(TAG, "Post ${post.id} has been successfully updated")
            }
        })
    }

    companion object {
        private val TAG = PostPresenter::class.java.simpleName
    }
}
