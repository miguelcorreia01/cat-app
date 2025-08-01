package com.example.catapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [CatBreedEntity::class], version = 1, exportSchema = false)
abstract class CatBreedDatabase : RoomDatabase() {

    abstract fun catBreedDao(): CatBreedDao

    companion object {
        const val DATABASE_NAME = "cat_breed_database"
    }

}