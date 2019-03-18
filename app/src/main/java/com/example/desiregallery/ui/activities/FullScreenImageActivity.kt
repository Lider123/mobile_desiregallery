package com.example.desiregallery.ui.activities

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.desiregallery.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_full_screen_image.*
import android.graphics.BitmapFactory
import android.R.attr.data
import android.os.Build
import android.view.WindowManager
import com.example.desiregallery.Utils


class FullScreenImageActivity : AppCompatActivity() {

    private lateinit var image: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT < 16)
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        else
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_full_screen_image)
        image = Utils.bytesToBitmap(intent.getByteArrayExtra("bytesImage"))
        image_view_full_screen.setImageBitmap(image)
    }
}
