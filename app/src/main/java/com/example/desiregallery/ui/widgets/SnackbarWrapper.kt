package com.example.desiregallery.ui.widgets

import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * @author babaetskv on 20.09.19
 */
class SnackbarWrapper(container: View) {
    private val snackbar: Snackbar = Snackbar.make(container, "", Snackbar.LENGTH_LONG)

    var currentText = ""
        private set

    fun show(text: String) {
        currentText = text
        snackbar.setText(currentText)
        snackbar.show()
    }

    fun isShown() = snackbar.isShown
}