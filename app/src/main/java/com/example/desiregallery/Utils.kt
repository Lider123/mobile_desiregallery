package com.example.desiregallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.app.Activity
import android.content.Context
import android.os.Environment
import android.util.Base64
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.desiregallery.logging.DGLogger
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    private val TAG = Utils::class.java.simpleName
    private const val DOWNLOAD_FOLDER_DEFAULT = "DesireDownloads/"

    fun bitmapToBytes(bmp: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun bytesToBitmap(bytes: ByteArray): Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

    fun hideSoftKeyboard(activity: Activity) {
        try {
            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
        catch (e: NullPointerException) {
            DGLogger.logWarning(TAG, "There was no keyboard to hide")
        }
    }

    fun downloadBitmap(bitmap: Bitmap, activity: Activity) {
        val tag = activity::class.java.simpleName
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
            DGLogger.logInfo(tag, "Image has been downloaded")
        } catch (e: IOException) {
            DGLogger.logError(tag, "Unable to download image")
            Toast.makeText(activity, R.string.download_error, Toast.LENGTH_LONG).show()
            e.printStackTrace()
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

    fun formatDate(context: Context, datetime: Long): String {
        val format = context.getString(R.string.datetime_format)
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return formatter.format(datetime)
    }
}