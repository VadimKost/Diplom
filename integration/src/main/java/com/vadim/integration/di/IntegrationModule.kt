package com.vadim.integration.di

import com.vadim.integration.camera.AndroidXCamera
import com.vadim.domain.integraion.Camera
import com.vadim.domain.integraion.EyesOpenedDetector
import com.vadim.domain.integraion.PeripheralConnector
import com.vadim.integration.eyesdetector.EyesOpenedML
import com.vadim.integration.integration.LocalServerPeripheralConnector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class RepositoryModule {

    @Provides
    @ActivityScoped
    fun providePeripheralConnector(impl: LocalServerPeripheralConnector): PeripheralConnector = impl

    @Provides
    @ActivityScoped
    fun provideEyesOpenedAlgorithm(impl: EyesOpenedML): EyesOpenedDetector = impl

    @Provides
    @ActivityScoped
    fun provideCamera(impl: AndroidXCamera): Camera = impl
}