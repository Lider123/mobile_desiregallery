package com.example.desiregallery.di

import android.view.View
import com.example.desiregallery.adapters.PostAdapter
import com.example.desiregallery.analytics.AnalyticsTracker
import com.example.desiregallery.analytics.IDGAnalyticsTracker
import com.example.desiregallery.models.Post
import com.example.desiregallery.presenters.ProfilePresenter
import com.example.desiregallery.sharedprefs.IDGSharedPreferencesHelper
import com.example.desiregallery.sharedprefs.PreferencesHelper
import com.example.desiregallery.ui.contracts.IProfileContract
import com.example.desiregallery.ui.widgets.SnackbarWrapper
import com.example.desiregallery.viewmodels.PostListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.dsl.module

/**
 * @author babaetskv on 29.10.19
 */
val applicationModule = module(override = true) {
    single<IDGSharedPreferencesHelper> { PreferencesHelper(androidContext()) }
    single<IDGAnalyticsTracker> { AnalyticsTracker(androidContext()) }
    factory { (container: View) -> SnackbarWrapper(container) }
    factory { (posts: MutableList<Post>) -> PostAdapter(posts) }
    factory<IProfileContract.Presenter> { (view: IProfileContract.View) -> ProfilePresenter(view) }
    viewModel<PostListViewModel>()
}
