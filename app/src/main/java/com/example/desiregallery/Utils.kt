package com.example.desiregallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.view.inputmethod.InputMethodManager
import android.util.Log
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * Class for some useful functions
 *
 * @author babaetskv
 * */
class Utils {
    companion object {

        /**
         * Default folder for downloads
         * */
        private const val DOWNLOAD_FOLDER_DEFAULT = "DesireDownloads/"

        /**
         * Method that converts bitmap to array of bytes
         *
         * @param bmp The bitmap to convert
         * @return The resulting array of bytes
         * */
        fun bitmapToBytes(bmp: Bitmap): ByteArray {
            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
            return stream.toByteArray()
        }

        /**
         * Method that converts array of bytes into bitmap
         *
         * @param bytes Array of bytes to convert
         * @return The resulting bitmap
         * */
        fun bytesToBitmap(bytes: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

        /**
         * Method for manual hiding software keyboard
         *
         * @param activity The activity from which is needed to remove the keyboard
         * */
        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }

        /**
         * Method that downloads bitmap to default downloads folder
         *
         * @param bitmap Bitmap to download
         * @param activity The activity from which is needed to download file
         * */
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

        /**
         * Method that converts bitmap to base64 string
         *
         * @param bitmap The bitmap to convert
         * @return The resulting base64 string
         * */
        fun bitmapToBase64(bitmap: Bitmap): String {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }

        /**
         * Method that converts base64 string into bitmap
         *
         * @param str Base64 string to convert
         * @return The resulting bitmap
         * */
        fun base64ToBitmap(str: String): Bitmap {
            val decodedBytes = Base64.decode(str, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        }

        /**
         * Method for image sharing
         *
         * @param image Image to share
         * @param context Context for external activity start
         * */
        fun shareImage(image: Bitmap, context: Context) {
            val bmpUri = getLocalBitmapUri(image, context)
            bmpUri.let {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, it)
                    type = "image/*"
                }
                context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_image)))
            }
        }

        /**
         * Method that returns the URI path to given bitmap
         *
         * @param bmp Bitmap to save temporarily in storage and get URI
         * @param context Context for getting access to system info
         * @return URI path of given bitmap
         * */
        private fun getLocalBitmapUri(bmp: Bitmap, context: Context): Uri? {
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
    }
}