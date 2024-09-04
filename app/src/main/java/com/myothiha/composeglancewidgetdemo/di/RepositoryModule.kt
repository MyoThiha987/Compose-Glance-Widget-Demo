package com.myothiha.composeglancewidgetdemo.di

import com.myothiha.composeglancewidgetdemo.data.WeatherRepositoryImpl
import com.myothiha.composeglancewidgetdemo.domain.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @Author Liam
 * Created at 03/Sep/2024
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository
}