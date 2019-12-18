package com.example.desiregallery

import com.example.desiregallery.auth.AuthMethod
import com.example.desiregallery.data.prefs.IDGSharedPreferencesHelper
import com.example.desiregallery.di.*
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

/**
 * @author Konstantin on 02.12.2019
 */
class SharedPreferencesHelperUnitTest {
    @Inject
    lateinit var mPrefsHelper: IDGSharedPreferencesHelper

    private var authMethod: AuthMethod? = null

    @Before
    fun before() {
        val component = DaggerTestAppComponent.builder()
            .appModule(TestAppModule())
            .authModule(TestAuthModule())
            .networkModule(TestNetworkModule())
            .analyticsModule(TestAnalyticsModule())
            .dataModule(TestDataModule())
            .build()
        component.inject(this)
    }

    @Test
    fun `first test`() {
        //every { mPrefsHelper.authMethod } returns authMethod
        every { mPrefsHelper.hasAuthMethod } returns (authMethod != null)
        //every { mPrefsHelper setProperty "authMethod" value AuthMethod.EMAIL } just run { authMethod = AuthMethod.EMAIL }
        every { mPrefsHelper getProperty "authMethod" } propertyType AuthMethod::class answers { authMethod }
        every { mPrefsHelper setProperty "authMethod" value any<AuthMethod>() } propertyType AuthMethod::class answers { authMethod = value }

        assertFalse(mPrefsHelper.hasAuthMethod)
        mPrefsHelper.authMethod = AuthMethod.EMAIL
        assertTrue(mPrefsHelper.hasAuthMethod)
        assertEquals(AuthMethod.EMAIL, mPrefsHelper.authMethod)
        mPrefsHelper.clearAuthMethod()
        assertFalse(mPrefsHelper.hasAuthMethod)
    }
}