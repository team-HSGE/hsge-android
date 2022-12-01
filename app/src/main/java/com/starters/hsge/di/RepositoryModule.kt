package com.starters.hsge.di

import com.starters.hsge.data.repository.DogProfileRepositoryImpl
import com.starters.hsge.domain.repository.DogProfileRepository
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

}