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
class AboutDialog(context: Context) : AlertDialog(context) {

    init {
        val message = TextView(context)
        message.setPadding(60, 40, 40, 40)
        val s = SpannableString(context.getString(R.string.about_message, getAppVersion(context)))
        Linkify.addLinks(s, Linkify.WEB_URLS)
        message.text = s
        message.movementMethod = LinkMovementMethod.getInstance()

        setTitle(R.string.app_name)
        setCancelable(true)
        setIcon(R.drawable.logo_icon)
        setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.OK)) { dialog, _ ->
            dialog.dismiss()
        }
        setView(message)
    }
}
