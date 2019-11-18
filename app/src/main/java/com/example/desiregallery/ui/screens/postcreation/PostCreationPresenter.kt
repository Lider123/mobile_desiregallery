package com.example.desiregallery.ui.screens.postcreation

import android.graphics.Bitmap
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.storage.IStorageHelper
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo

/**
 * @author babaetskv on 18.11.19
 */
class PostCreationPresenter(
    private val accProvider: AccountProvider,
    private val storageHelper: IStorageHelper
) : IPostCreationContract.Presenter {
    private lateinit var view: IPostCreationContract.View
    private lateinit var image: Bitmap
    private lateinit var callback: IPostCreationListener

    override fun attach(view: IPostCreationContract.View, image: Bitmap, listener: IPostCreationListener) {
        this.view = view
        this.image = image
        this.callback = listener
    }

    override fun handlePublish() {
        view.showProgress()
        val post = Post()
        post.author = User("", "").apply {
            login = accProvider.currAccount?.displayName?: ""
            photo = accProvider.currAccount?.photoUrl?: ""
        }
        storageHelper.uploadPostImage(image, post.id, object: IStorageHelper.Callback {

            override fun onComplete(resultUrl: String) {
                logInfo(TAG, "Image for new post ${post.id} successfully uploaded")
                post.setImageUrl(resultUrl)
                view.hideProgress()
                callback.onPostCreationSubmit(post)
                view.finish()
            }

            override fun onFailure(error: Exception) {
                logError(TAG, "Failed to upload image for new post ${post.id}: ${error.message}")
                view.hideProgress()
                view.updateErrorMessageVisibility(true)
            }
        })
    }

    override fun handleCancel() {
        callback.onPostCreationCancel()
        view.finish()
    }

    companion object {
        private val TAG = PostCreationPresenter::class.java.simpleName
    }
}