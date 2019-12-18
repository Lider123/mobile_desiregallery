package com.example.desiregallery.ui.screens.postcreation

import android.graphics.Bitmap
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.Result
import com.example.desiregallery.data.storage.IStorageHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

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

    override fun attach(
        view: IPostCreationContract.View,
        image: Bitmap,
        listener: IPostCreationListener
    ) {
        this.view = view
        this.image = image
        this.callback = listener
    }

    override fun handlePublish() {
        val post = Post()
        post.author = User("", "").apply {
            login = accProvider.currAccount?.displayName ?: ""
            photo = accProvider.currAccount?.photoUrl ?: ""
        }
        GlobalScope.launch(Dispatchers.Main) {
            view.showProgress()
            when (val result = storageHelper.uploadPostImage(image, post.id)) {
                is Result.Success -> {
                    Timber.i("Image for new post ${post.id} successfully uploaded")
                    post.setImageUrl(result.data)
                    view.hideProgress()
                    callback.onSubmit(post.apply { timestamp = Date().time })
                    view.finish()
                }
                is Result.Error -> {
                    Timber.e(result.exception, "Failed to upload image for new post ${post.id}")
                    view.hideProgress()
                    view.updateErrorMessageVisibility(true)
                }
            }
        }
    }

    override fun handleCancel() {
        callback.onCancel()
        view.finish()
    }
}
