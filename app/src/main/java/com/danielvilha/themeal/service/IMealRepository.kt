package com.danielvilha.themeal.service

import com.danielvilha.themeal.data.dto.CategoryDto
import com.danielvilha.themeal.data.dto.MealDetailsDto
import com.danielvilha.themeal.data.dto.MealShortDto

interface IMealRepository {
    suspend fun getCategories(): List<CategoryDto>
    suspend fun getMeals(category: String): List<MealShortDto>
    suspend fun getMealDetails(id: String): MealDetailsDto?
}