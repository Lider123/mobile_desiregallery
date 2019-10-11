package com.example.desiregallery.ui.views

interface PostView {
    fun updateRating(rating: Float)
    fun updateImage(imageUrl: String)
    fun updateAuthorName(name: String)
    fun updateAuthorPhoto(imageUrl: String)
    fun updateTimestamp(time: Long)
}