package com.example.desiregallery.presenters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import com.example.desiregallery.Utils
import com.example.desiregallery.models.Post
import com.example.desiregallery.ui.activities.CommentsActivity
import com.example.desiregallery.ui.activities.FullScreenImageActivity
import com.example.desiregallery.ui.views.PostView

class PostPresenter(
    private val view: PostView,
    private val post: Post = Post()
) {

    fun setRating() {
        view.updateRating(post.getRating())
    }

    fun updateRating(rate: Float) {
        post.updateRating(rate)
        view.updateRating(post.getRating())
    }

    fun setImageView() {
        view.updateImage(post.getImageUrl().toString())
    }

    fun goToCommentActivity(context: Context) {
        val intent = Intent(context, CommentsActivity::class.java)
        intent.putExtra(CommentsActivity.EXTRA_POST, post)
        context.startActivity(intent)
    }

    fun goToImageFullScreen(context: Context, bmpImage: Bitmap) {
        val intent = Intent(context, FullScreenImageActivity::class.java)
        intent.putExtra(FullScreenImageActivity.EXTRA_IMAGE, Utils.bitmapToBytes(bmpImage))
        context.startActivity(intent)
    }
}
