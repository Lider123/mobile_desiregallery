package com.example.desiregallery.ui.screens.postcreation

import android.graphics.Bitmap

/**
 * @author babaetskv on 18.11.19
 */
interface IPostCreationContract {

    interface View {
        fun showProgress()
        fun hideProgress()
        fun updateErrorMessageVisibility(visible: Boolean)
        fun finish()
    }

    interface Presenter {
        fun attach(view: View, image: Bitmap, listener: IPostCreationListener)
        fun handlePublish()
        fun handleCancel()
    }
}