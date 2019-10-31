package com.example.desiregallery.storage

import android.graphics.Bitmap

/**
 * @author babaetskv on 31.10.19
 */
interface IStorageHelper {
    fun uploadProfileImage(bitmap: Bitmap, userLogin: String, callback: Callback)
    fun uploadPostImage(bitmap: Bitmap, postId: String, callback: Callback)

    interface Callback {
        fun onComplete(resultUrl: String)
        fun onFailure(error: Exception)
    }
}