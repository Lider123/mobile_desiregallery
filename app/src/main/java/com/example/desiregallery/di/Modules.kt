package com.example.desiregallery.di

import android.content.res.Resources
import com.example.desiregallery.analytics.AnalyticsTracker
import com.example.desiregallery.analytics.IDGAnalyticsTracker
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.network.BaseNetworkService
import com.example.desiregallery.data.network.NetworkUtils
import com.example.desiregallery.data.network.QueryNetworkService
import com.example.desiregallery.data.prefs.IDGSharedPreferencesHelper
import com.example.desiregallery.data.prefs.PreferencesHelper
import com.example.desiregallery.data.storage.IStorageHelper
import com.example.desiregallery.data.storage.StorageHelper
import com.example.desiregallery.ui.screens.comments.CommentListViewModel
import com.example.desiregallery.ui.presenters.IPostContract
import com.example.desiregallery.ui.screens.feed.PostListViewModel
import com.example.desiregallery.ui.presenters.PostPresenter
import com.example.desiregallery.ui.screens.profile.IProfileContract
import com.example.desiregallery.ui.screens.profile.ProfilePresenter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author babaetskv on 29.10.19
 */
val applicationModule = module(override = true) {
    single<Resources> { androidContext().resources }
    single<IDGSharedPreferencesHelper> { PreferencesHelper(androidContext()) }
    single<IDGAnalyticsTracker> { AnalyticsTracker(androidContext()) }
    single<IStorageHelper> { StorageHelper(get()) }
    single { AccountProvider() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseAuth.getInstance() }
    single {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(androidContext(), gso)
    }

    factory<IProfileContract.Presenter> { (view: IProfileContract.View) -> ProfilePresenter(view) }
    factory<IPostContract.Presenter> { (view: IPostContract.View, post: Post) ->
        PostPresenter(
            view,
            post
        )
    }
}

val networkModule = module {
    single { QueryNetworkService.getService() }
    single { BaseNetworkService.createService() }
    single { NetworkUtils() }
}

val viewModelModule = module {
    viewModel { PostListViewModel(get(), get()) }
    viewModel { (postId: String) -> CommentListViewModel(get(), postId, get()) }
}
