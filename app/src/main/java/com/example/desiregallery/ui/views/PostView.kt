package com.example.desiregallery.ui.views

/**
 * Interface that defines actions with the post view
 *
 * @author babaetskv
 * */
interface PostView {
    /**
     * Method for rating updates
     * */
    fun updateRating(rating: Float)

    /**
     * Method for image updates
     * */
    fun updateImage(imageUrl: String)
}