package com.example.desiregallery.di

import com.example.desiregallery.di.modules.AppModule
import io.mockk.mockk

/**
 * @author Konstantin on 01.12.2019
 */
class TestAppModule : AppModule(mockk())
