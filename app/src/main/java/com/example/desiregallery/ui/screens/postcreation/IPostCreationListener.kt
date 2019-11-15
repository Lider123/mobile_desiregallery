package com.example.desiregallery.ui.screens.postcreation

import com.example.desiregallery.data.models.Post

/**
 * @author babaetskv on 18.11.19
 */
interface IPostCreationListener {
    fun onPostCreationSubmit(post: Post)
    fun onPostCreationCancel()
}