package com.example.desiregallery.data.storage

import android.graphics.Bitmap
import com.example.desiregallery.data.Result

/**
 * @author babaetskv on 31.10.19
 */
interface IStorageHelper {
    suspend fun uploadProfileImage(bitmap: Bitmap, userLogin: String): Result<String>
    suspend fun uploadPostImage(bitmap: Bitmap, postId: String): Result<String>
}