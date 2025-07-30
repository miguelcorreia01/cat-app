package com.example.catapp.data.api

import com.google.gson.annotations.SerializedName

data class CatBreedDto(
    val id: String,
    val name: String,
    val description: String,
    val temperament: String,
    val origin: String,
    @SerializedName("life_span") val lifeSpan: String,
    @SerializedName("reference_image_id") val referenceImageId: String
)

