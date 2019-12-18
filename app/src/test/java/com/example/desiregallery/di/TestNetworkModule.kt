package com.example.desiregallery.di

import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.di.modules.NetworkModule
import io.mockk.mockk

/**
 * @author Konstantin on 01.12.2019
 */
class TestNetworkModule : NetworkModule() {

    override fun provideNetworkManager(): NetworkManager = mockk()
}
