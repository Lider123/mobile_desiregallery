package com.example.desiregallery.presenters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import com.example.desiregallery.Utils
import com.example.desiregallery.models.Post
import com.example.desiregallery.network.DGNetwork
import com.example.desiregallery.ui.activities.CommentsActivity
import com.example.desiregallery.ui.activities.FullScreenImageActivity
import com.example.desiregallery.ui.views.PostView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostPresenter(
    private val view: PostView,
    private val post: Post = Post()
) {
    companion object {
        private val TAG = PostPresenter::class.java.simpleName
    }

    fun setRating() {
        view.updateRating(post.getRating())
    }

    fun updateRating(rate: Float) {
        post.updateRating(rate)
        view.updateRating(post.getRating())
        DGNetwork.getService().updatePost(post.getId(), post).enqueue(object: Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                Log.e(TAG, "Unable to update post ${post.getId()}: ${t.message}")
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful)
                    Log.i(TAG, "Post ${post.getId()} has been successfully updated")
            }
        })
    }

    fun setImageView() {
        view.updateImage(post.getImageUrl().toString())
    }

    fun goToCommentActivity(context: Context) {
        val intent = Intent(context, CommentsActivity::class.java).apply { putExtra(CommentsActivity.EXTRA_POST, post) }
        context.startActivity(intent)
    }

    fun goToImageFullScreen(context: Context, bmpImage: Bitmap) {
        val intent = Intent(context, FullScreenImageActivity::class.java).apply {
            putExtra(FullScreenImageActivity.EXTRA_IMAGE, Utils.bitmapToBytes(bmpImage))
        }
        context.startActivity(intent)
    }
}
