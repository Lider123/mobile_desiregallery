package com.example.desiregallery.di.components

import com.example.desiregallery.di.modules.*
import com.example.desiregallery.ui.screens.postcreation.PostCreationDialog
import com.example.desiregallery.ui.screens.postcreation.PostCreationFragment
import com.example.desiregallery.ui.screens.*
import com.example.desiregallery.ui.screens.auth.LoginFragment
import com.example.desiregallery.ui.screens.auth.SignUpActivity
import com.example.desiregallery.ui.screens.feed.FeedFragment
import com.example.desiregallery.ui.screens.post.PostViewHolder
import com.example.desiregallery.ui.screens.profile.EditProfileFragment
import com.example.desiregallery.ui.screens.profile.ProfileFragment
import com.example.desiregallery.ui.screens.post.SmallPostViewHolder
import dagger.Component
import javax.inject.Singleton

/**
 * @author babaetskv on 12.11.19
 */
@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        AuthModule::class,
        AnalyticsModule::class,
        DataModule::class,
        ProfileModule::class,
        PostsModule::class,
        PostCreationModule::class,
        FeedModule::class
    ]
)
interface AppComponent {
    fun inject(activity: SignUpActivity)
    fun inject(activity: MainActivity)
    fun inject(activity: FullScreenImageActivity)
    fun inject(activity: SplashScreenActivity)
    fun inject(activity: ShareReceiverActivity)

    fun inject(fragment: EditProfileFragment)
    fun inject(fragment: FeedFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: PostCreationFragment)

    fun inject(viewHolder: SmallPostViewHolder)
    fun inject(viewHolder: PostViewHolder)

    fun inject(dialog: PostCreationDialog)

    fun plusCommentsComponent(module: CommentsModule): CommentsComponent
}
