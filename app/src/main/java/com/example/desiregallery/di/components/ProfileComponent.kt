package com.example.desiregallery.di.components

import com.example.desiregallery.di.modules.ProfileModule
import com.example.desiregallery.di.scopes.ProfileScope
import com.example.desiregallery.ui.screens.profile.ProfileFragment
import dagger.Subcomponent

/**
 * @author babaetskv on 09.12.19
 */
@ProfileScope
@Subcomponent(modules = [ProfileModule::class])
interface ProfileComponent {
    fun inject(fragment: ProfileFragment)
}
