package com.example.desiregallery.ui.screens.postcreation

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.desiregallery.MainApplication
import com.example.desiregallery.R
import com.example.desiregallery.utils.toBitmap
import com.example.desiregallery.utils.toBytes
import kotlinx.android.synthetic.main.layout_create_post.*
import javax.inject.Inject

/**
 * @author babaetskv on 18.11.19
 */
class PostCreationFragment private constructor() : Fragment(),
    IPostCreationContract.View,
    View.OnClickListener {
    @Inject
    lateinit var presenter: PostCreationPresenter

    private lateinit var mPostCreationListener: IPostCreationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainApplication.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.layout_create_post, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrayOf(post_creation_publish, post_creation_cancel).map { it.setOnClickListener(this) }
        arguments?.let {
            val imageBytes = it.getByteArray(ARG_IMAGE_BYTES)
            val image = imageBytes!!.toBitmap()
            dialog_post_image.setImageBitmap(image)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (requireActivity() as? IPostCreationListener)?.let {
            mPostCreationListener = it
        } ?: throw Exception("Parent activity doesn't implement post creation interface")
    }

    override fun onStart() {
        super.onStart()
        val image = (dialog_post_image.drawable as BitmapDrawable).bitmap
        presenter.attach(this, image, mPostCreationListener)
    }

    override fun showProgress() {
        dialog_post_progress.visibility = View.VISIBLE
        post_creation_publish.isEnabled = false
        post_creation_cancel.isEnabled = false
        updateErrorMessageVisibility(false)
    }

    override fun hideProgress() {
        dialog_post_progress.visibility = View.GONE
        post_creation_publish.isEnabled = true
        post_creation_cancel.isEnabled = true
    }

    override fun updateErrorMessageVisibility(visible: Boolean) {
        error_message.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun finish() {
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.post_creation_publish -> presenter.handlePublish()
            R.id.post_creation_cancel -> presenter.handleCancel()
        }
    }

    companion object {
        const val ARG_IMAGE_BYTES = "imageBytes"

        fun createInstance(image: Bitmap): PostCreationFragment {
            val instance = PostCreationFragment()
            Bundle().apply {
                val imageBytes = image.toBytes()
                putByteArray(ARG_IMAGE_BYTES, imageBytes)
            }.also {
                instance.arguments = it
            }
            return instance
        }
    }
}
