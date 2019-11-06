@file:JvmName("BaseUtils")
package com.example.desiregallery.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.example.desiregallery.R
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*
import android.content.pm.PackageManager
import java.text.ParseException

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

fun getAgeFromBirthday(birthday: String): Int? {
    var date: Date? = null
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    try {
        date = sdf.parse(birthday)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    date?: return null

    val dob = Calendar.getInstance().apply { time = date }
    val today = Calendar.getInstance()

    val year = dob.get(Calendar.YEAR)
    val month = dob.get(Calendar.MONTH)
    val day = dob.get(Calendar.DAY_OF_MONTH)

    dob.set(year, month + 1, day)

    var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR))
        age--

    return age
}

fun getAppVersion(context: Context): String {
    return try {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        pInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        ""
    }
}
