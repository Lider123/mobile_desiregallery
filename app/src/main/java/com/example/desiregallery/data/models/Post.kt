package com.example.desiregallery.data.models

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.net.MalformedURLException
import java.net.URL
import kotlin.random.Random

data class Post(
    @SerializedName("author")
    var author: User = User("", ""),
    @SerializedName("imageUrl")
    var imageUrl: URL? = null,
    @SerializedName("rating")
    var rating: Float = 0f,
    @SerializedName("numOfRates")
    var numOfRates: Int = 0,
    @SerializedName("timestamp")
    var timestamp: Long = 0L
) : Serializable {
    @SerializedName("id")
    var id = Random.nextLong(1e10.toLong()).toString()

    fun setImageUrl(imageUrl: String) {
        try {
            this.imageUrl = URL(imageUrl)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
    }

    fun updateRating(rate: Float) {
        rating = (rating * numOfRates + rate) / (numOfRates + 1)
        numOfRates++
    }

    companion object {
        val CALLBACK = object : DiffUtil.ItemCallback<Post>() {

            override fun areItemsTheSame(oldItem: Post, newItem: Post) = (oldItem.id == newItem.id)

            override fun areContentsTheSame(oldItem: Post, newItem: Post) = true
        }
    }
}
