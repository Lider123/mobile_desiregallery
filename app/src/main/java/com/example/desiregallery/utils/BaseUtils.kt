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
import android.content.pm.PackageManager

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

fun getAppVersion(context: Context): String {
    return try {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        pInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        ""
    }
}
