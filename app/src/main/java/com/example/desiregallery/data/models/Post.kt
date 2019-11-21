package com.example.desiregallery.data.models

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.net.MalformedURLException
import java.net.URL
import kotlin.random.Random

class Post : Serializable {
    @SerializedName("id")
    var id = Random.nextLong(1e10.toLong()).toString()
    @SerializedName("author")
    var author: User = User("", "")
    @SerializedName("imageUrl")
    var imageUrl: URL? = null
    @SerializedName("rating")
    var rating = 0f
    @SerializedName("numOfRates")
    var numOfRates = 0
    @SerializedName("timestamp")
    var timestamp = 0L

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Post

        if (id != other.id) return false
        if (author != other.author) return false
        if (imageUrl != other.imageUrl) return false
        if (rating != other.rating) return false
        if (numOfRates != other.numOfRates) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + (imageUrl?.hashCode() ?: 0)
        result = 31 * result + rating.hashCode()
        result = 31 * result + numOfRates
        result = 31 * result + timestamp.hashCode()
        return result
    }

    override fun toString() =
        "Post(id='$id', author=$author, imageUrl=$imageUrl, rating=$rating, numOfRates=$numOfRates, timestamp=$timestamp)"

    companion object {
        val CALLBACK = object : DiffUtil.ItemCallback<Post>() {

            override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Post, newItem: Post) = true
        }
    }
}
