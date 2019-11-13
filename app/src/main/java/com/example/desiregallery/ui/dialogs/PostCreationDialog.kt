package com.example.desiregallery.ui.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
) : AlertDialog(activity) {
    @Inject
    lateinit var accProvider: AccountProvider
    @Inject
    lateinit var storageHelper: IStorageHelper

    private lateinit var content: View

    override fun onCreate(savedInstanceState: Bundle?) {
        MainApplication.appComponent.inject(this)
        content = View.inflate(context, R.layout.dialog_create_post, null)
        setView(content)
        setTitle(R.string.post_creation)
        setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.Publish)) { _, _ -> handlePublish() }
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.Cancel)) { _, _ -> handleCancel() }
        content.dialog_post_image.setImageBitmap(image)
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
                onPublish(post)
            }

            override fun onFailure(error: Exception) {
                logError(TAG, "Failed to upload image for new post ${post.id}: ${error.message}")
                updateErrorMessageVisibility(true)
            }
        })
        hideProgress()
        dismiss()
    }

    private fun handleCancel() = dismiss()

    private fun showProgress() {
        content.dialog_post_progress.visibility = View.VISIBLE
        getButton(BUTTON_POSITIVE).isEnabled = false
        getButton(BUTTON_NEGATIVE).isEnabled = false
        updateErrorMessageVisibility(false)
    }

    private fun hideProgress() {
        content.dialog_post_progress.visibility = View.GONE
        getButton(BUTTON_POSITIVE).isEnabled = true
        getButton(BUTTON_NEGATIVE).isEnabled = true
    }

    private fun updateErrorMessageVisibility(visible: Boolean) {
        content.error_message.visibility = if (visible) View.VISIBLE else View.GONE
    }

    companion object {
        private val TAG = PostCreationDialog::class.java.simpleName
    }
}
