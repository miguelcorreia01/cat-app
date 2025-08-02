package com.example.catapp.di

import com.example.catapp.data.CatBreedRepositoryImpl
import com.example.catapp.domain.repository.CatBreedRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCatBreedRepository(
        catBreedRepositoryImpl: CatBreedRepositoryImpl
    ): CatBreedRepository


}
