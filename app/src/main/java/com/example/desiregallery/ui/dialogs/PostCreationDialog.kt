package com.example.desiregallery.ui.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.desiregallery.R
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.storage.IStorageHelper
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
import kotlinx.android.synthetic.main.dialog_create_post.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * @author babaetskv
 * @since 24.07.19
 */

class PostCreationDialog(
    private val activity: Activity,
    private val image: Bitmap,
    private val onPublish: (Post) -> Unit
) : AlertDialog(activity), KoinComponent {
    private val accProvider: AccountProvider by inject()
    private val storageHelper: IStorageHelper by inject()

    private lateinit var content: View

    override fun onCreate(savedInstanceState: Bundle?) {
        content = View.inflate(context, R.layout.dialog_create_post, null)
        setView(content)
        setTitle(R.string.post_creation)
        setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.Publish)) { dialog, which ->
            dialog as AlertDialog
            dialog.getButton(which).isEnabled = false
            dialog.getButton(BUTTON_NEGATIVE).isEnabled = false
            handlePublish()
        }
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.Cancel)) { _, _ -> handleCancel() }
        content.dialog_post_image.setImageBitmap(image)
        setCancelable(false)

        super.onCreate(savedInstanceState)
    }

    private fun handlePublish() {
        content.dialog_post_progress.visibility = View.VISIBLE
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
                Toast.makeText(activity, R.string.post_image_upload_failure, Toast.LENGTH_LONG)
                    .show()
            }
        })
        content.dialog_post_progress.visibility = View.GONE
        dismiss()
    }

    private fun handleCancel() = dismiss()

    companion object {
        private val TAG = PostCreationDialog::class.java.simpleName
    }
}
