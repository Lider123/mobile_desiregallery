package com.example.desiregallery.data.storage

import android.graphics.Bitmap
import com.example.desiregallery.utils.bitmapToBytes
import com.google.firebase.storage.FirebaseStorage

/**
 * @author babaetskv on 31.10.19
 */
class StorageHelper(private val storage: FirebaseStorage) : IStorageHelper {

    override fun uploadProfileImage(bitmap: Bitmap, userLogin: String,
                                    callback: IStorageHelper.Callback) {
        uploadImage(bitmap, "$STORAGE_PROFILE_IMAGES_DIR/$userLogin.jpg", callback)
    }

    override fun uploadPostImage(bitmap: Bitmap, postId: String,
                                 callback: IStorageHelper.Callback) {
        uploadImage(bitmap, "$STORAGE_POST_IMAGES_DIR/$postId.jpg", callback)
    }

    private fun uploadImage(bitmap: Bitmap, path: String, callback: IStorageHelper.Callback) {
        val imageRef = storage.getReferenceFromUrl(STORAGE_URL).child(path)
        val uploadTask = imageRef.putBytes(bitmapToBytes(bitmap))
        uploadTask.addOnFailureListener { error ->
            callback.onFailure(error)
        }.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                callback.onFailure(Exception("Upload task wasn't successful"))
                return@addOnCompleteListener
            }

            imageRef.downloadUrl.addOnCompleteListener { uriTask ->
                callback.onComplete(uriTask.result.toString())
            }
        }
    }

    companion object {
        private const val STORAGE_URL = "gs://desiregallery-8072a.appspot.com"
        private const val STORAGE_POST_IMAGES_DIR = "postImages"
        private const val STORAGE_PROFILE_IMAGES_DIR = "profileImages"
    }
}
