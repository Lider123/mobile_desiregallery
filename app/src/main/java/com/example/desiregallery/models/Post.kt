package com.example.desiregallery.models

import java.io.Serializable
import java.net.MalformedURLException
import java.net.URL

class Post : Serializable {

    private var id = ""
    private var imageUrl: URL? = null
    private var rating = 0.0f
    private var numOfRates = 0
    private var comments = emptyList<String>()

    fun getId() = id

    fun setId(id: String) {
        this.id = id
    }

    fun getImageUrl() = imageUrl

    fun setImageUrl(imageUrl: String) {
        try {
            this.imageUrl = URL(imageUrl)
        }
        catch(e: MalformedURLException) {
            e.printStackTrace()
        }
    }

    fun getRating() = rating

    fun setRating(rating: Float) {
        this.rating = rating
    }

    fun setNumOfRates(numOfRates: Int) {
        this.numOfRates = numOfRates
    }

    fun getComments() = comments

    fun setComments(comments: List<String>) {
        this.comments = comments
    }

    fun updateRating(rate: Float) {
        rating = (rating * numOfRates + rate) / (numOfRates + 1)
        numOfRates++
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (javaClass != other?.javaClass)
            return false

        other as Post

        if (id != other.id)
            return false
        if (imageUrl != other.imageUrl)
            return false
        if (rating != other.rating)
            return false
        if (numOfRates != other.numOfRates)
            return false
        if (comments != other.comments)
            return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (imageUrl?.hashCode() ?: 0)
        result = 31 * result + rating.hashCode()
        result = 31 * result + numOfRates
        result = 31 * result + comments.hashCode()
        return result
    }


}
