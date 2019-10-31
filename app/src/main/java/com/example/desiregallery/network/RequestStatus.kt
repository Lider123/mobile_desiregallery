package com.example.desiregallery.network

import com.example.desiregallery.MainApplication
import com.example.desiregallery.R

/**
 * @author babaetskv on 20.09.19
 */
enum class RequestStatus(val message: String) {
    ERROR_DOWNLOAD(MainApplication.instance.applicationContext.getString(R.string.comments_download_error)),
    ERROR_UPLOAD(MainApplication.instance.applicationContext.getString(R.string.comment_upload_error)),
    SUCCESS("Success"),
    DOWNLOADING("Loading"),
}