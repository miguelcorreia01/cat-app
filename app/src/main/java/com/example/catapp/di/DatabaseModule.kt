package com.example.catapp.di

import android.content.Context
import androidx.room.Room
import com.example.catapp.data.local.CatBreedDao
import com.example.catapp.data.local.CatBreedDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCatBreedDatabase(@ApplicationContext context: Context): CatBreedDatabase {
        return Room.databaseBuilder(
            context,
            CatBreedDatabase::class.java,
            "cat_breed_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCatBreedDao(database: CatBreedDatabase): CatBreedDao {
        return database.catBreedDao()
    }

}