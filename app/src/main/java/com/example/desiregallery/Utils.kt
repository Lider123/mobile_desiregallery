package com.example.desiregallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.app.Activity
import android.os.Environment
import android.view.inputmethod.InputMethodManager
import android.util.Log
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class Utils {
    companion object {
        private const val DOWNLOAD_FOLDER_DEFAULT = "DesireDownloads/"

        fun bitmapToBytes(bmp: Bitmap): ByteArray {
            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
            return stream.toByteArray()
        }

        fun bytesToBitmap(bytes: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
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
                Log.i(tag, "Image has been downloaded")
            } catch (e: IOException) {
                Log.e(tag, "Unable to download image")
                Toast.makeText(activity, R.string.download_error, Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }
}