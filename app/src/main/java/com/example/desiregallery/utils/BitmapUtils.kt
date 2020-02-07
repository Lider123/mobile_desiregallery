@file:JvmName("BitmapUtils")

package com.example.desiregallery.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import android.content.ContentResolver
import android.provider.MediaStore

private const val DOWNLOAD_FOLDER_DEFAULT = "DesireDownloads/"

fun Bitmap.toBytes(): ByteArray = ByteArrayOutputStream().also {
    compress(Bitmap.CompressFormat.PNG, 100, it)
}.toByteArray()

fun ByteArray.toBitmap(): Bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)

fun Bitmap.download(callback: DownloadCallback) {
    val root = Environment.getExternalStorageDirectory()
    val path = File(root, DOWNLOAD_FOLDER_DEFAULT)
    path.mkdir()
    val n = Random().nextInt(1e10.toInt()).toString().padStart(10, '0')
    val file = File(path, "$n.jpg")
    try {
        val out = FileOutputStream(file).also {
            this.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        out.flush()
        out.close()
        callback.onSuccess()
    } catch (e: IOException) {
        callback.onFailure(e)
    }
}

fun Bitmap.toBase64(): String {
    val byteArray = ByteArrayOutputStream().also {
        this.compress(Bitmap.CompressFormat.PNG, 100, it)
    }.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun base64ToBitmap(str: String): Bitmap {
    val decodedBytes = Base64.decode(str, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

/**
 * Method returns the URI path to given bitmap
 *
 * @param bmp Bitmap to save temporarily in storage and get URI
 * @return URI path of given bitmap
 * */
fun Bitmap.getLocalUri(context: Context): Uri? {
    var bmpUri: Uri? = null
    try {
        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "share_image_" + System.currentTimeMillis() + ".png"
        )
        val out = FileOutputStream(file)
        this.compress(Bitmap.CompressFormat.PNG, 90, out)
        out.close()
        bmpUri = Uri.fromFile(file)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return bmpUri
}

fun getBitmapFromUri(uri: Uri, resolver: ContentResolver): Bitmap =
    MediaStore.Images.Media.getBitmap(resolver, uri)

interface DownloadCallback {
    fun onSuccess()
    fun onFailure(e: Exception)
}
