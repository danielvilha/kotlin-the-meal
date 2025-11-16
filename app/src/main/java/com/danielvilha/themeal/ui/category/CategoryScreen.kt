package com.danielvilha.themeal.ui.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.danielvilha.themeal.data.dto.MealShortDto
import com.danielvilha.themeal.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.themeal.ui.preview.LightDarkPreview
import com.danielvilha.themeal.ui.theme.TheMealTheme
import com.danielvilha.themeal.ui.util.ErrorText
import com.danielvilha.themeal.ui.util.ProgressIndicator

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun ScreenPreview(
    @PreviewParameter(CategoryScreenPreview ::class)
    state: CategoryUiState
) {
    TheMealTheme {
        CategoryScreen(
            category = "Beef",
            state = state,
            onBackClick = {},
            onMealClick = {},
            onRetryClick = {}
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    category: String,
    state: CategoryUiState,
    onBackClick: () -> Unit,
    onMealClick: (String) -> Unit,
    onRetryClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(category) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> ProgressIndicator()
                state.error != null -> ErrorText(error = state.error, onRetry = onRetryClick)
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.meals) { meal ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onMealClick(meal.idMeal) },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = meal.strMealThumb,
                                        contentDescription = meal.strMeal,
                                        modifier = Modifier
                                            .size(72.dp)
                                            .clip(RoundedCornerShape(12.dp)),
                                        contentScale = ContentScale.Crop
                                    )

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = meal.strMeal,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
class CategoryScreenPreview : PreviewParameterProvider<CategoryUiState> {
    override val values: Sequence<CategoryUiState>
        get() = sequenceOf(
            CategoryUiState(
                isLoading = false,
                meals = listOf(
                    MealShortDto(
                        idMeal = "53099",
                        strMeal = "Aussie Burgers",
                        strMealThumb = "https:www.themealdb.com/images/media/meals/44bzep1761848278.jpg",
                    )
                ),
                error = null
            )
        )
}