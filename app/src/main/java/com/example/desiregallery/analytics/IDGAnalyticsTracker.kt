package com.example.desiregallery.analytics

import com.example.desiregallery.auth.AuthMethod

/**
 * @author babaetskv on 18.09.19
 */
interface IDGAnalyticsTracker {
    fun trackLogin(method: AuthMethod)
    fun trackSignUp(method: AuthMethod)
    fun trackSharePhoto(itemId: String)
}