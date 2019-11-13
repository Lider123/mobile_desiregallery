package com.example.desiregallery.di.components

import com.example.desiregallery.di.modules.CommentsModule
import com.example.desiregallery.di.scopes.CommentsScope
import com.example.desiregallery.ui.screens.comments.CommentsActivity
import dagger.Subcomponent

/**
 * @author babaetskv on 12.11.19
 */
@CommentsScope
@Subcomponent(modules = [CommentsModule::class])
interface CommentsComponent {
    fun inject(activity: CommentsActivity)
}