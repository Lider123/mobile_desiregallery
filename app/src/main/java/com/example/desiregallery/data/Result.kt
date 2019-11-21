package com.example.desiregallery.data

import java.lang.Exception

/**
 * @author babaetskv on 20.11.19
 */
sealed class Result<out T : Any> {
    data class Success<T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}
