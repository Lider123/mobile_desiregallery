package com.example.desiregallery.ui.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import com.example.desiregallery.MainApplication
import com.example.desiregallery.R
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.storage.IStorageHelper
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
import kotlinx.android.synthetic.main.dialog_create_post.view.*
import javax.inject.Inject

/**
 * @author babaetskv
 * @since 24.07.19
 */
class PostCreationDialog(
    activity: Activity,
    private val image: Bitmap,
    private val onPublish: (Post) -> Unit
) : Dialog(activity) {
    @Inject
    lateinit var accProvider: AccountProvider
    @Inject
    lateinit var storageHelper: IStorageHelper

    private lateinit var content: View

    override fun onCreate(savedInstanceState: Bundle?) {
        MainApplication.appComponent.inject(this)
        content = View.inflate(context, R.layout.dialog_create_post, null).apply {
            post_creation_publish.setOnClickListener { handlePublish() }
            post_creation_cancel.setOnClickListener { handleCancel() }
            dialog_post_image.setImageBitmap(image)
        }.also {
            setContentView(it)
        }
        setTitle(R.string.post_creation)
        setCancelable(false)

        super.onCreate(savedInstanceState)
    }

    private fun handlePublish() {
        showProgress()
        val post = Post()
        post.author = User("", "").apply {
            login = accProvider.currAccount?.displayName?: ""
            photo = accProvider.currAccount?.photoUrl?: ""
        }
        storageHelper.uploadPostImage(image, post.id, object: IStorageHelper.Callback {

            override fun onComplete(resultUrl: String) {
                logInfo(TAG, "Image for new post ${post.id} successfully uploaded")
                post.setImageUrl(resultUrl)
                hideProgress()
                dismiss()
                onPublish(post)
            }

            override fun onFailure(error: Exception) {
                logError(TAG, "Failed to upload image for new post ${post.id}: ${error.message}")
                hideProgress()
                updateErrorMessageVisibility(true)
            }
        })
    }

    private fun handleCancel() = dismiss()

    private fun showProgress() {
        with(content) {
            dialog_post_progress.visibility = View.VISIBLE
            post_creation_publish.isEnabled = false
            post_creation_cancel.isEnabled = false
        }
        updateErrorMessageVisibility(false)
    }

    private fun hideProgress() {
        with(content) {
            dialog_post_progress.visibility = View.GONE
            post_creation_publish.isEnabled = true
            post_creation_cancel.isEnabled = true
        }
    }

    private fun updateErrorMessageVisibility(visible: Boolean) {
        content.error_message.visibility = if (visible) View.VISIBLE else View.GONE
    }

    companion object {
        private val TAG = PostCreationDialog::class.java.simpleName
    }
}
