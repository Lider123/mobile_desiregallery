package com.example.desiregallery

import com.example.desiregallery.data.Result
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.di.*
import io.mockk.every
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

/**
 * @author Konstantin on 01.12.2019
 */
class NetworkManagerUnitTest {
    @Inject
    lateinit var mNetworkManager: NetworkManager

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
        assertNotNull(mNetworkManager)
        every {
            runBlocking {
                mNetworkManager.getUser("login")
            }
        } returns Result.Success(User().apply { login = "login" })
        val result = runBlocking { mNetworkManager.getUser("login") }
        assert(result is Result.Success)
        result as Result.Success
        assertEquals("login", result.data.login)
    }
}