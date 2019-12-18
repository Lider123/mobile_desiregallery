package com.example.desiregallery.data.storage

import android.graphics.Bitmap
import com.example.desiregallery.data.Result
import com.example.desiregallery.utils.bitmapToBytes
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * @author babaetskv on 31.10.19
 */
class StorageHelper(private val storage: FirebaseStorage) : IStorageHelper {

    override suspend fun uploadProfileImage(bitmap: Bitmap, userLogin: String): Result<String> =
        uploadImage(bitmap, "$STORAGE_PROFILE_IMAGES_DIR/$userLogin.jpg")

    override suspend fun uploadPostImage(bitmap: Bitmap, postId: String): Result<String> =
        uploadImage(bitmap, "$STORAGE_POST_IMAGES_DIR/$postId.jpg")

    private suspend fun uploadImage(bitmap: Bitmap, path: String): Result<String> =
        withContext(Dispatchers.IO) {
            val imageRef = storage.getReferenceFromUrl(STORAGE_URL).child(path)
            try {
                val uploadTask = imageRef.putBytes(bitmapToBytes(bitmap))
                uploadTask.await()
                if (!uploadTask.isSuccessful) return@withContext Result.Error(IOException("Upload task wasn't successful"))

                val url = imageRef.downloadUrl.await().toString()
                return@withContext Result.Success(url)
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }

    companion object {
        private const val STORAGE_URL = "gs://desiregallery-8072a.appspot.com"
        private const val STORAGE_POST_IMAGES_DIR = "postImages"
        private const val STORAGE_PROFILE_IMAGES_DIR = "profileImages"
    }
}
