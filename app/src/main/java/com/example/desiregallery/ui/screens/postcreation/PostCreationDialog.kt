package com.example.desiregallery.ui.screens.postcreation

import android.app.Activity
import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import com.example.desiregallery.MainApplication
import com.example.desiregallery.R
import kotlinx.android.synthetic.main.layout_create_post.view.*
import javax.inject.Inject

/**
 * @author babaetskv
 * @since 24.07.19
 */
class PostCreationDialog(
    activity: Activity,
    private val image: Bitmap,
    private val callback: IPostCreationListener
) : Dialog(activity), IPostCreationContract.View {
    @Inject
    lateinit var presenter: PostCreationPresenter

    private lateinit var content: View

    override fun onCreate(savedInstanceState: Bundle?) {
        MainApplication.appComponent.inject(this)
        content = View.inflate(context, R.layout.layout_create_post, null).apply {
            post_creation_publish.setOnClickListener { presenter.handlePublish() }
            post_creation_cancel.setOnClickListener { presenter.handleCancel() }
            dialog_post_image.setImageBitmap(image)
        }.also {
            setContentView(it)
        }
        presenter.attach(this, image, callback)
        setTitle(R.string.post_creation)
        setCancelable(false)

        super.onCreate(savedInstanceState)
    }

    override fun showProgress() {
        with(content) {
            dialog_post_progress.visibility = View.VISIBLE
            post_creation_publish.isEnabled = false
            post_creation_cancel.isEnabled = false
        }
        updateErrorMessageVisibility(false)
    }

    override fun hideProgress() {
        with(content) {
            dialog_post_progress.visibility = View.GONE
            post_creation_publish.isEnabled = true
            post_creation_cancel.isEnabled = true
        }
    }

    override fun updateErrorMessageVisibility(visible: Boolean) {
        content.error_message.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun finish() {
        dismiss()
    }
}
