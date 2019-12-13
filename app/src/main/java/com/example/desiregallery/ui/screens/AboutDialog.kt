package com.example.desiregallery.ui.screens

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.TextView
import com.example.desiregallery.R
import com.example.desiregallery.utils.getAppVersion

/**
 * @author babaetskv on 28.10.19
 */
class AboutDialog(context: Context) : AlertDialog(context, R.style.CustomAlertDialog) {

    init {
        val message = TextView(context).apply {
            setPadding(60, 40, 40, 40)
            text = SpannableString(
                context.getString(R.string.about_message, getAppVersion(context))
            ).also {
                Linkify.addLinks(it, Linkify.WEB_URLS)
            }
            movementMethod = LinkMovementMethod.getInstance()
        }

        setTitle(R.string.app_name)
        setCancelable(true)
        setIcon(R.drawable.logo_icon)
        setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.OK)) { dialog, _ ->
            dialog.dismiss()
        }
        setView(message)
    }
}
