package com.example.desiregallery.di.components

import com.example.desiregallery.di.modules.*
import com.example.desiregallery.ui.dialogs.PostCreationDialog
import com.example.desiregallery.ui.screens.LoginActivity
import com.example.desiregallery.ui.screens.MainActivity
import com.example.desiregallery.ui.screens.SettingsFragment
import com.example.desiregallery.ui.screens.SignUpActivity
import com.example.desiregallery.ui.screens.feed.FeedFragment
import com.example.desiregallery.ui.screens.feed.PostViewHolder
import com.example.desiregallery.ui.screens.profile.EditProfileFragment
import com.example.desiregallery.ui.screens.profile.ProfileFragment
import com.example.desiregallery.ui.screens.profile.SmallPostViewHolder
import com.example.desiregallery.ui.screens.FullScreenImageActivity
import com.example.desiregallery.ui.screens.SplashScreenActivity
import dagger.Component
import javax.inject.Singleton

/**
 * @author babaetskv on 12.11.19
 */
@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    AuthModule::class,
    AnalyticsModule::class,
    DataModule::class,
    ProfileModule::class,
    PostsModule::class
])
interface AppComponent {
    fun inject(activity: SignUpActivity)
    fun inject(activity: MainActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: FullScreenImageActivity)
    fun inject(activity: SplashScreenActivity)

    fun inject(fragment: EditProfileFragment)
    fun inject(fragment: FeedFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: SettingsFragment)

    fun inject(viewHolder: SmallPostViewHolder)
    fun inject(viewHolder: PostViewHolder)

    fun inject(dialog: PostCreationDialog)

    fun plusCommentsComponent(module: CommentsModule): CommentsComponent
}
