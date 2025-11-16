package com.danielvilha.themeal.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.danielvilha.themeal.ui.categories.CategoryListScreen
import com.danielvilha.themeal.ui.categories.CategoryListViewModel

@Composable
fun CategoryListRoute(
    onCategoryClick: (String) -> Unit,
    viewModel: CategoryListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCategories()
    }

    CategoryListScreen(
        state = state,
        onCategoryClick = onCategoryClick,
        onRetryClick = { viewModel.loadCategories() }
    )
}