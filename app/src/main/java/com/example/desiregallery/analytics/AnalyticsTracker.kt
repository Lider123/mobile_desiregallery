package com.example.desiregallery.analytics

import android.content.Context
import android.os.Bundle
import com.example.desiregallery.auth.AuthMethod
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * @author babaetskv on 18.09.19
 */
class AnalyticsTracker(context: Context) : IDGAnalyticsTracker {
    private val analytics = FirebaseAnalytics.getInstance(context)

    override fun trackLogin(method: AuthMethod) {
        Bundle().apply {
            putString(FirebaseAnalytics.Param.METHOD, method.name)
        }.also {
            analytics.logEvent(FirebaseAnalytics.Event.LOGIN, it)
        }
    }

    override fun trackSignUp(method: AuthMethod) {
        Bundle().apply {
            putString(FirebaseAnalytics.Param.METHOD, method.name)
        }.also {
            analytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, it)
        }
    }

    override fun trackSharePhoto(itemId: String) {
        Bundle().apply {
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, "post_image")
            putString(FirebaseAnalytics.Param.ITEM_ID, itemId)
        }.also {
            analytics.logEvent(FirebaseAnalytics.Event.SHARE, it)
        }
    }
}
