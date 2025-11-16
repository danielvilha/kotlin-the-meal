package com.danielvilha.themeal.ui.categories

import com.danielvilha.themeal.data.dto.CategoryDto

data class CategoryListUiState(
    val isLoading: Boolean = false,
    val categories: List<CategoryDto> = emptyList(),
    val error: String? = null
)
