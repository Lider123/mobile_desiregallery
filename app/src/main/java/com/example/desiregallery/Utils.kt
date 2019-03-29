package com.example.desiregallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import android.app.Activity
import android.view.inputmethod.InputMethodManager
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        private const val sDateFormat = "yyyy-MM-dd HH:mm:ss"

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

        fun dateToString(date: Date): String {
            val dateFormat = SimpleDateFormat(sDateFormat, Locale.getDefault())
            return dateFormat.format(date)
        }

        fun stringToDate(date: String): Date? {
            val dateFormat = SimpleDateFormat(sDateFormat, Locale.getDefault())
            try {
                return dateFormat.parse(date)
            } catch (e: ParseException) {
                return null
            }
        }
    }
}