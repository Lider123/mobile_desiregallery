package com.example.desiregallery.ui.activities

import android.Manifest
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.desiregallery.R
import com.example.desiregallery.Utils
import kotlinx.android.synthetic.main.activity_full_screen_image.*
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.util.Log

/**
 * @author babaetskv
 * */
class FullScreenImageActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_IMAGE = "bytesImage"
    }

    private val TAG = FullScreenImageActivity::class.java.simpleName
    private val WRITE_REQUEST_CODE = 101

    private lateinit var toolbar: Toolbar

    private lateinit var currImage: Bitmap

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

        currImage = Utils.bytesToBitmap(intent.getByteArrayExtra(EXTRA_IMAGE))
        image_view_full_screen.setImageBitmap(currImage)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_image_screen, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.image_download -> {
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, permissions, WRITE_REQUEST_CODE)
                true
            }
            R.id.image_share -> {
                Utils.shareImage(currImage, this)
                true
            }
            else -> false
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            WRITE_REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.downloadBitmap(currImage, this)
            } else {
                Log.e(TAG, "There is no permission to write to external storage")
                Toast.makeText(this, R.string.no_access_to_storage, Toast.LENGTH_LONG).show()
            }
        }
    }
}
