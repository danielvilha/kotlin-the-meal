package com.danielvilha.themeal.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.danielvilha.themeal.features.mealdetails.MealDetailsScreen
import com.danielvilha.themeal.features.mealdetails.MealDetailsViewModel

@Composable
fun MealDetailsRoute(
    mealId: String,
    onBackClick: () -> Unit,
    viewModel: MealDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(mealId) {
        viewModel.loadMealDetails(mealId)
    }

    MealDetailsScreen(
        state = state,
        onBackClick = onBackClick,
        onRetryClick = { viewModel.loadMealDetails(mealId) }
    )
}