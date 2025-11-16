package com.danielvilha.themeal.service

import com.danielvilha.themeal.data.response.CategoriesResponse
import com.danielvilha.themeal.data.response.MealDetailsResponse
import com.danielvilha.themeal.data.response.MealListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {
    @GET("categories.php")
    suspend fun getCategories(): CategoriesResponse

    @GET("filter.php")
    suspend fun getMealsByCategory(
        @Query("c") category: String
    ): MealListResponse

    @GET("lookup.php")
    suspend fun getMealDetails(
        @Query("i") recipe_id: String
    ): MealDetailsResponse
}