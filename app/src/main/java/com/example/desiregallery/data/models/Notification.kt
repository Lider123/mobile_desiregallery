package com.example.desiregallery.data.models

import com.google.gson.annotations.SerializedName
import kotlin.random.Random

/**
 * @author babaetskv on 25.11.19
 */
data class Notification(
    @SerializedName("loginTo")
    var loginTo: String,
    @SerializedName("message")
    var message: String
) {
    @SerializedName("id")
    val id = Random.nextLong(1e10.toLong()).toString()
}
