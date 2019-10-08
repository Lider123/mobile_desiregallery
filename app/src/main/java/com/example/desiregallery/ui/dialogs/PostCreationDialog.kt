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
import com.example.desiregallery.Utils
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.logging.DGLogger
import com.example.desiregallery.models.Post
import com.example.desiregallery.models.User
import kotlinx.android.synthetic.main.dialog_create_post.view.*

/**
 * @author babaetskv
 * @since 24.07.19
 */

class PostCreationDialog(private val activity: Activity, private val image: Bitmap, private val onPublish: (Post) -> Unit) : AlertDialog(activity) {
    companion object {
        private val TAG = PostCreationDialog::class.java.simpleName
    }

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
            login = AccountProvider.currAccount?.displayName?: ""
            photo = AccountProvider.currAccount?.photoUrl?: ""
        }
        val imageRef = MainApplication.storage.getReferenceFromUrl(MainApplication.STORAGE_URL).child("${MainApplication.STORAGE_POST_IMAGES_DIR}/${post.id}.jpg")
        val uploadTask = imageRef.putBytes(Utils.bitmapToBytes(image))
        uploadTask.addOnFailureListener { error ->
            DGLogger.logError(TAG, "Failed to upload image for new post ${post.id}: ${error.message}")
            Toast.makeText(activity, R.string.post_image_upload_failure, Toast.LENGTH_LONG).show()
        }.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                DGLogger.logError(TAG, "Image for new post ${post.id} has not been uploaded")
                Toast.makeText(activity, R.string.post_image_upload_failure, Toast.LENGTH_LONG).show()
                return@addOnCompleteListener
            }

            DGLogger.logInfo(TAG, "Image for new post ${post.id} successfully uploaded")
            imageRef.downloadUrl.addOnCompleteListener { uriTask ->
                post.setImageUrl(uriTask.result.toString())
                onPublish(post)
            }
        }
        content.dialog_post_progress.visibility = View.GONE
        dismiss()
    }

    private fun handleCancel() = dismiss()
}