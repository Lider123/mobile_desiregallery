package com.example.desiregallery.ui.activities

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.desiregallery.R
import com.example.desiregallery.Utils
import kotlinx.android.synthetic.main.activity_full_screen_image.*


class FullScreenImageActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_IMAGE = "bytesImage"
    }

    private lateinit var toolbar: Toolbar

    private lateinit var image: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT < 16)
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        else
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_full_screen_image)

        toolbar = findViewById(R.id.image_screen_toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)

        image = Utils.bytesToBitmap(intent.getByteArrayExtra(EXTRA_IMAGE))
        image_view_full_screen.setImageBitmap(image)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_image_screen, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.image_download -> {
                // TODO
                Toast.makeText(this, "Download item have been clicked", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.image_share -> {
                // TODO
                Toast.makeText(this, "Share item have been clicked", Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }
}
