package com.danielvilha.themeal.features.category

import com.danielvilha.themeal.data.dto.MealShortDto

data class CategoryUiState(
    val isLoading: Boolean = false,
    val meals: List<MealShortDto> = emptyList(),
    val error: String? = null
)
