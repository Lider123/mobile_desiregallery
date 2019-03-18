package com.example.desiregallery.ui.dialogs

import android.app.Dialog
import android.content.Context
import kotlinx.android.synthetic.main.dialog_rate.*
import android.widget.RatingBar.OnRatingBarChangeListener
import com.example.desiregallery.R
import kotlin.math.ceil


class ImageRateDialog(context: Context, onRate: (Float) -> Unit) : Dialog(context) {
    init {
        setContentView(R.layout.dialog_rate)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog_rate_bar.onRatingBarChangeListener = OnRatingBarChangeListener { ratingBar, rating, fromUser ->
            onRate(ceil(rating))
        }
    }
}