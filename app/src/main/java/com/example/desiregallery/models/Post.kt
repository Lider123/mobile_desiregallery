package com.example.desiregallery.models

import java.net.MalformedURLException
import java.net.URL

class Post {

    private var id : String = ""
    private var imageUrl : URL = URL("")
    private var rating : Float = 0.0f
    private var comments : List<String> = ArrayList()

    fun getId() : String {
        return id
    }

    fun setId(id : String) {
        this.id = id
    }

    fun getImageUrl() : URL {
        return imageUrl
    }

    fun setImageUrl(imageUrl : URL) {
        this.imageUrl = imageUrl
    }

    fun setImageUrl(imageUrl : String) {
        try {
            this.imageUrl = URL(imageUrl)
        }
        catch(e : MalformedURLException) {
            e.printStackTrace()
        }
    }

    fun getRating() : Float {
        return rating
    }

    fun setRating(rating : Float) {
        this.rating = rating
    }

    fun getComments() : List<String> {
        return comments
    }

    fun setComments(comments : List<String>) {
        this.comments = comments
    }
}
