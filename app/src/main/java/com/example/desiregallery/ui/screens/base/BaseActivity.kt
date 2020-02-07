package com.example.desiregallery.ui.screens.base

import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import timber.log.Timber

/**
 * @author babaetskv on 07.02.20
 */
abstract class BaseActivity : AppCompatActivity() {

    protected fun setToolbar(@IdRes toolbarRes: Int, title: String, backEnabled: Boolean = false) {
        findViewById<Toolbar>(toolbarRes).apply {
            this.title = title
        }.also {
            setSupportActionBar(it)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(backEnabled)
    }

    protected fun showToast(message: String, duration: Int) {
        Toast.makeText(this, message, duration).show()
    }

    protected fun showToast(@StringRes resId: Int, duration: Int) {
        Toast.makeText(this, resId, duration).show()
    }

    protected fun hideSoftKeyboard() {
        try {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: NullPointerException) {
            Timber.w("There was no keyboard to hide")
        }
    }
}
