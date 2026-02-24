package com.speze88.namenstag.di

import com.speze88.namenstag.data.repository.ContactRepository
import com.speze88.namenstag.data.repository.ContactRepositoryImpl
import com.speze88.namenstag.data.repository.NameDayRepository
import com.speze88.namenstag.data.repository.NameDayRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNameDayRepository(impl: NameDayRepositoryImpl): NameDayRepository

    @Binds
    @Singleton
    abstract fun bindContactRepository(impl: ContactRepositoryImpl): ContactRepository
}
