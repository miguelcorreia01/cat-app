package com.example.catapp.data.api

import retrofit2.http.GET
import retrofit2.http.Path


interface CatApiService {
    @GET("breeds")
    suspend fun getCatBreeds(): List<CatBreedDto>

    @GET("images/{imageId}")
    suspend fun getImageById(@Path("imageId") imageId: String): ImageDto

}