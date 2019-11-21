package com.example.desiregallery.ui.screens

import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.desiregallery.R
import kotlinx.android.synthetic.main.activity_full_screen_image.*
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import com.example.desiregallery.MainApplication
import com.example.desiregallery.analytics.IDGAnalyticsTracker
import com.example.desiregallery.ui.widgets.SnackbarWrapper
import com.example.desiregallery.utils.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import timber.log.Timber
import javax.inject.Inject

class FullScreenImageActivity : AppCompatActivity() {
    @Inject
    lateinit var analytics: IDGAnalyticsTracker

    private lateinit var toolbar: Toolbar
    private lateinit var snackbar: SnackbarWrapper
    private lateinit var image: Bitmap
    private lateinit var postId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_full_screen_image)
        MainApplication.appComponent.inject(this)
        snackbar = SnackbarWrapper(full_screen_container)

        toolbar = findViewById(R.id.image_screen_toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)
        Picasso.with(this)
            .load(imageUrl)
            .error(R.drawable.image_error)
            .into(image_view_full_screen, object : Callback {

                override fun onSuccess() {
                    image = (image_view_full_screen.drawable as BitmapDrawable).bitmap
                }

                override fun onError() {
                    Timber.e("There was an error while getting the bitmap")
                }
            })

        postId = intent.getStringExtra(EXTRA_POST_ID)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_image_screen, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.image_download -> {
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, permissions, WRITE_REQUEST_CODE)
                true
            }
            R.id.image_share -> {
                shareImage()
                true
            }
            else -> false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            WRITE_REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadBitmap(image, object : DownloadCallback {

                    override fun onSuccess() {
                        Timber.i("Image has been downloaded")
                        snackbar.show(getString(R.string.download_success))
                    }

                    override fun onFailure(e: Exception) {
                        Timber.e("Unable to download image")
                        snackbar.show(getString(R.string.download_error))
                    }
                })
            } else {
                Timber.w("There is no permission to write to external storage")
                snackbar.show(getString(R.string.no_access_to_storage))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SHARING_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                analytics.trackSharePhoto(postId)
                snackbar.show(getString(R.string.share_success))
            } else snackbar.show(getString(R.string.share_canceled))
        }
    }

    private fun shareImage() {
        val bmpUri = getLocalBitmapUri(image, this)
        bmpUri?.let {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, it)
            shareIntent.type = "image/*"
            startActivityForResult(
                Intent.createChooser(shareIntent, getString(R.string.share_image)),
                SHARING_REQUEST_CODE
            )
        }
    }

    companion object {
        private const val WRITE_REQUEST_CODE = 101
        private const val SHARING_REQUEST_CODE = 201

        const val EXTRA_IMAGE_URL = "imageUrl"
        const val EXTRA_POST_ID = "postId"
    }
}
