package com.danielvilha.themeal.ui.meal

import com.danielvilha.themeal.data.dto.MealDetailsDto

data class MealDetailsUiState(
    val isLoading: Boolean = false,
    val selectedMeal: MealDetailsDto? = null,
    val error: String? = null
)
