package com.example.desiregallery.ui.activities

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.example.desiregallery.R
import com.example.desiregallery.Utils
import kotlinx.android.synthetic.main.activity_full_screen_image.*


class FullScreenImageActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_IMAGE = "bytesImage"
    }

    private lateinit var image: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT < 16)
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        else
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_full_screen_image)
        image = Utils.bytesToBitmap(intent.getByteArrayExtra(EXTRA_IMAGE))
        image_view_full_screen.setImageBitmap(image)
    }
}
