package com.example.catapp.data.api

import retrofit2.http.GET


interface CatApiService {
    @GET("breeds")
    suspend fun getCatBreeds(): List<CatBreedDto>
}