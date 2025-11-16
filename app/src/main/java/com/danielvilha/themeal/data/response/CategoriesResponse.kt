package com.danielvilha.themeal.data.response

import com.danielvilha.themeal.data.dto.CategoryDto

data class CategoriesResponse(
    val categories: List<CategoryDto>
)