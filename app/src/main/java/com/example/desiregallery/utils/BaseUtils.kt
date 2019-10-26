@file:JvmName("BaseUtils")
package com.example.desiregallery.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.example.desiregallery.R
import com.example.desiregallery.logging.logWarning
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "BaseUtils"

fun hideSoftKeyboard(activity: Activity) {
    try {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }
    catch (e: NullPointerException) {
        logWarning(TAG, "There was no keyboard to hide")
    }
}

fun formatDate(context: Context, datetime: Long): String {
    val format = context.getString(R.string.datetime_format)
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(datetime)
}
