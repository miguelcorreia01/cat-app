package com.example.catapp.data

import com.example.catapp.data.api.CatBreedDto
import com.example.catapp.data.local.CatBreedEntity
import com.example.catapp.domain.model.CatBreed

object CatBreedMapper {

    private fun CatBreedDto.toCatBreed(): CatBreed {
        val lifeSpanValue = parseLifeSpanHighestValue(lifeSpan)

        return CatBreed(
            id = id,
            name = name,
            description = description,
            temperament = temperament,
            origin = origin,
            lifeSpan = lifeSpanValue,
            isFavorite = false,
            image = ""
        )
    }

    private fun CatBreed.toCatBreedEntity(): CatBreedEntity {
        return CatBreedEntity(
            id = id,
            name = name,
            description = description,
            temperament = temperament,
            origin = origin,
            lifeSpan = lifeSpan,
            isFavorite = isFavorite,
            image = image
        )
    }

    private fun CatBreedEntity.toCatBreed(): CatBreed {
        return CatBreed(
            id = id,
            name = name,
            description = description,
            temperament = temperament,
            origin = origin,
            lifeSpan = lifeSpan,
            isFavorite = isFavorite,
            image = image
        )
    }

    fun List<CatBreedDto>.toCatBreedList(): List<CatBreed> {
        return this.map { it.toCatBreed() }
    }

    fun List<CatBreedEntity>.toCatBreedList(): List<CatBreed> {
        return this.map { it.toCatBreed() }
    }

    fun List<CatBreed>.toCatBreedEntityList(): List<CatBreedEntity> {
        return this.map { it.toCatBreedEntity() }
    }

    private fun parseLifeSpanHighestValue(lifeSpan: String): Int {
        return try {
            val parts = lifeSpan.split("-")
            parts[1].trim().toIntOrNull() ?: 0
        } catch (e: Exception) {
            0
        }
    }
}