package com.danielvilha.themeal

import com.danielvilha.themeal.data.dto.CategoryDto
import com.danielvilha.themeal.data.dto.MealDetailsDto
import com.danielvilha.themeal.data.dto.MealShortDto
import com.danielvilha.themeal.service.IMealRepository
import com.danielvilha.themeal.service.NetworkModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

class FakeMealRepository : IMealRepository {
    override suspend fun getCategories(): List<CategoryDto> {
        return listOf(CategoryDto(idCategory = "1", strCategory = "Beef", strCategoryThumb = "", strCategoryDescription = ""))
    }

    override suspend fun getMeals(category: String): List<MealShortDto> {
        return listOf(MealShortDto(strMeal = "Beef and Mustard Pie", strMealThumb = "", idMeal = "52874"))
    }

    override suspend fun getMealDetails(id: String): MealDetailsDto? {
        return MealDetailsDto(idMeal = "52874", strMeal = "Beef and Mustard Pie")
    }
}

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
object TestNetworkModule {

    @Singleton
    @Provides
    fun provideMealRepository(): IMealRepository {
        return FakeMealRepository()
    }
}