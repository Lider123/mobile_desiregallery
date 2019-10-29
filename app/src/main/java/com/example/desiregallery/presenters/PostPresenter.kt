package com.example.desiregallery.presenters

import android.content.Context
import android.content.Intent
import com.example.desiregallery.logging.*
import com.example.desiregallery.models.Post
import com.example.desiregallery.network.baseService
import com.example.desiregallery.ui.activities.CommentsActivity
import com.example.desiregallery.ui.activities.FullScreenImageActivity
import com.example.desiregallery.ui.contracts.IPostContract
import com.example.desiregallery.ui.dialogs.ImageRateDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostPresenter(private val post: Post = Post()): IPostContract.Presenter {
    private lateinit var view: IPostContract.View

    override fun attach(view: IPostContract.View) {
        this.view = view
        view.updateImage(post.imageUrl.toString())
        view.updateRating(post.rating)
        view.updateAuthorName(post.author.login)
        view.updateAuthorPhoto(post.author.photo)
        view.updateTimestamp(post.timestamp)
    }

    override fun onImageClick(context: Context) {
        val imageUrl = post.imageUrl
        imageUrl?.let {
            goToImageFullScreen(context)
        }
    }

    override fun onRatingClick(context: Context) {
        val rateDialog = ImageRateDialog(context) { rate -> updateRating(rate) }
        rateDialog.show()
    }

    override fun onCommentsClick(context: Context) {
        goToCommentActivity(context)
    }

    private fun goToCommentActivity(context: Context) {
        val intent = Intent(context, CommentsActivity::class.java).apply { putExtra(CommentsActivity.EXTRA_POST, post) }
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
        baseService.updatePost(post.id, post).enqueue(object: Callback<Post> {
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
