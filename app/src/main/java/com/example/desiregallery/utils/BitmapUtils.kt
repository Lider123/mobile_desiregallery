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

private const val DOWNLOAD_FOLDER_DEFAULT = "DesireDownloads/"

fun bitmapToBytes(bmp: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

fun bytesToBitmap(bytes: ByteArray): Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

fun downloadBitmap(bitmap: Bitmap, callback: DownloadCallback) {
    val root = Environment.getExternalStorageDirectory()
    val path = File(root, DOWNLOAD_FOLDER_DEFAULT)
    path.mkdir()
    val n = Random().nextInt(1e10.toInt()).toString().padStart(10, '0')
    val file = File(path, "$n.jpg")
    try {
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.flush()
        out.close()
        callback.onSuccess()
    } catch (e: IOException) {
        callback.onFailure(e)
    }
}

fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
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
fun getLocalBitmapUri(bmp: Bitmap, context: Context): Uri? {
    var bmpUri: Uri? = null
    try {
        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "share_image_" + System.currentTimeMillis() + ".png"
        )
        val out = FileOutputStream(file)
        bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
        out.close()
        bmpUri = Uri.fromFile(file)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return bmpUri
}

interface DownloadCallback {
    fun onSuccess()
    fun onFailure(e: Exception)
}
