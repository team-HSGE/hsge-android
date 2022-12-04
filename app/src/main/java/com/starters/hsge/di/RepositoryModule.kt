package com.starters.hsge.di

import com.starters.hsge.data.repository.DogOptionRepositoryImpl
import com.starters.hsge.data.repository.DogProfileRepositoryImpl
import com.starters.hsge.data.repository.RegisterRepositoryImpl
import com.starters.hsge.domain.repository.DogOptionRepository
import com.starters.hsge.domain.repository.DogProfileRepository
import com.starters.hsge.domain.repository.RegisterRepository
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
    abstract fun bindsDogProfileRepository(
        dogProfileRepositoryImp: DogProfileRepositoryImpl
    ): DogProfileRepository

    @Binds
    @Singleton
    abstract fun bindsDogOptionRepository(
        dogOptionRepositoryImpl: DogOptionRepositoryImpl
    ): DogOptionRepository

    @Binds
    @Singleton
    abstract fun bindsRegisterRepository(
        registerRepositoryImpl: RegisterRepositoryImpl
    ): RegisterRepository

}