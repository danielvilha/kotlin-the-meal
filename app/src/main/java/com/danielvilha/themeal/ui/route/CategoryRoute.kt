package com.danielvilha.themeal.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.danielvilha.themeal.ui.category.CategoryScreen
import com.danielvilha.themeal.ui.category.CategoryViewModel

@Composable
fun CategoryRoute(
    category: String,
    onBackClick: () -> Unit,
    onMealClick: (String) -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(category) {
        viewModel.loadMeals(category)
    }

    CategoryScreen(
        category = category,
        state = state,
        onBackClick = onBackClick,
        onMealClick = onMealClick,
        onRetryClick = { viewModel.loadMeals(category) }
    )
}