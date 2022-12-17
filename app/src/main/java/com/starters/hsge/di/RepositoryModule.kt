package com.starters.hsge.di

import com.starters.hsge.data.repository.*
import com.starters.hsge.domain.repository.*
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
    abstract fun bindsDogOptionRepository(
        dogOptionRepositoryImpl: DogOptionRepositoryImpl
    ): DogOptionRepository

    @Binds
    @Singleton
    abstract fun bindsRegisterRepository(
        registerRepositoryImpl: RegisterRepositoryImpl
    ): RegisterRepository

    @Binds
    @Singleton
    abstract fun bindsRegisterPreferencesRepository(
        registerPreferencesRepositoryImpl: RegisterPreferencesRepositoryImpl
    ): RegisterPreferencesRepository

    @Binds
    @Singleton
    abstract fun bindsMyDogRepository(
        myDogRepositoryImpl: MyDogRepositoryImpl
    ): MyDogRepository

    @Binds
    @Singleton
    abstract fun bindsEditDogRepository(
        editDogProfileRepositoryImpl: EditDogProfileRepositoryImpl
    ): EditDogProfileRepository

}