package com.example.desiregallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

class Utils {
    companion object {
        fun bitmapToBytes(bmp: Bitmap): ByteArray {
            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
            return stream.toByteArray()
        }

        fun bytesToBitmap(bytes: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }
}