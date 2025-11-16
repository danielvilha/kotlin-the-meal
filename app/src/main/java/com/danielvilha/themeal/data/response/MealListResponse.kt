package com.danielvilha.themeal.data.response

import com.danielvilha.themeal.data.dto.MealShortDto

data class MealListResponse(
    val meals: List<MealShortDto>
)
