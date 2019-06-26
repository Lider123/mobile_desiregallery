package com.example.desiregallery.models

import java.io.Serializable
import java.net.MalformedURLException
import java.net.URL

/**
 * Class that represents post
 *
 * Post contains image, rating in stars and comments
 *
 * @author babaetskv
 * */
class Post : Serializable {
    /**
     * Post's unique identifier
     * */
    private var id: String = ""

    /**
     * URL of post's image
     * */
    private var imageUrl: URL? = null

    /**
     * Rating of the image in stars (from 0 to 5)
     * */
    private var rating: Float = 0.0f

    /**
     * Number of rates. Is needed for rating updates
     * */
    private var numOfRates: Int = 0

    /**
     * List of comments
     * */
    private var comments: List<String> = ArrayList()

    fun getId() = id

    fun setId(id: String) {
        this.id = id
    }

    fun getImageUrl() = imageUrl

    fun setImageUrl(imageUrl: URL) {
        this.imageUrl = imageUrl
    }

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

    fun getNumOfRates() = numOfRates

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
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Post

        if (id != other.id) return false
        if (imageUrl != other.imageUrl) return false
        if (rating != other.rating) return false
        if (numOfRates != other.numOfRates) return false
        if (comments != other.comments) return false

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
