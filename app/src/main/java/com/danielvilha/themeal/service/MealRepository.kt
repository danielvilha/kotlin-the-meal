package com.danielvilha.themeal.service

import com.danielvilha.themeal.data.dto.CategoryDto
import com.danielvilha.themeal.data.dto.MealDetailsDto
import com.danielvilha.themeal.data.dto.MealShortDto
import javax.inject.Inject

class MealRepository @Inject constructor(
    private val api: MealApiService
) : IMealRepository {
    override suspend fun getCategories(): List<CategoryDto> {
        return api.getCategories().categories
    }

    override suspend fun getMeals(category: String): List<MealShortDto> {
        return api.getMealsByCategory(category).meals
    }

    override suspend fun getMealDetails(id: String): MealDetailsDto? {
        return api.getMealDetails(id).meals.firstOrNull()
    }
}