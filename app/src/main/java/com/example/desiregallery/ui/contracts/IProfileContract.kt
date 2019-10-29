package com.example.desiregallery.ui.contracts

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri

/**
 * @author babaetskv on 29.10.19
 */
interface IProfileContract {
    interface View {
        fun updateTitle(title: String)
        fun updateGender(gender: String)
        fun updateBirthday(birthday: String)
        fun updatePhoto(photoUrl: String)
        fun updatePhoto(bitmap: Bitmap)
        fun updatePhotoFabVisibility(visible: Boolean)
    }

    interface Presenter {
        val infoChanged: Boolean
        fun attach(resources: Resources)
        fun onGenderClick(context: Context)
        fun onBirthdayClick(context: Context)
        fun updateProfile(activity: Activity)
        fun uploadPhoto(imageUri: Uri, resolver: ContentResolver, onFailure: () -> Unit, onComplete: () -> Unit)
    }
}