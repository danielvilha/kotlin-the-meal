package com.danielvilha.themeal.ui.categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.danielvilha.themeal.data.dto.CategoryDto
import com.danielvilha.themeal.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.themeal.ui.preview.LightDarkPreview
import com.danielvilha.themeal.ui.theme.TheMealTheme
import com.danielvilha.themeal.ui.util.ErrorText
import com.danielvilha.themeal.ui.util.ProgressIndicator
import kotlin.math.abs

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun ScreenPreview(
    @PreviewParameter(CategoryListScreenPreview ::class)
    state: CategoryListUiState
) {
    TheMealTheme {
        CategoryListScreen(
            state = state,
            onCategoryClick = {},
            onRetryClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(
    state: CategoryListUiState,
    onCategoryClick: (String) -> Unit,
    onRetryClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categories") }
            )
        },
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
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(state.categories) { category ->
                            CategoryGridItem(category = category, onCategoryClick = onCategoryClick)
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun CategoryGridItem(
    category: CategoryDto,
    onCategoryClick: (String) -> Unit
) {
    val hue = (abs(category.strCategory.hashCode()) % 360f)
    val isDark = isSystemInDarkTheme()

    val categoryColor = if (isDark) {
        Color.hsv(
            hue = hue,
            saturation = 0.5f, // More saturation for dark theme
            value = 0.6f      // Darker value for dark theme
        )
    } else {
        Color.hsv(
            hue = hue,
            saturation = 0.3f, // Less saturation for light theme
            value = 0.95f     // Lighter value for light theme
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .clickable {
                onCategoryClick(category.strCategory)
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = categoryColor)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = category.strCategoryThumb,
                contentDescription = category.strCategory,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = category.strCategory,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = category.strCategoryDescription,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@ExcludeFromJacocoGeneratedReport
class CategoryListScreenPreview : PreviewParameterProvider<CategoryListUiState> {
    override val values: Sequence<CategoryListUiState>
        get() = sequenceOf(
            CategoryListUiState(
                isLoading = false,
                categories = listOf(
                    CategoryDto(
                        idCategory = "1",
                        strCategory = "Beef",
                        strCategoryThumb = "https://www.themealdb.com/images/category/beef.png",
                        strCategoryDescription = "Beef is the culinary name for meat from cattle, particularly skeletal muscle. Humans have been eating beef since prehistoric times.[1] Beef is a source of high-quality protein and essential nutrients.[2]"
                    ),
                    CategoryDto(
                        idCategory = "2",
                        strCategory = "Chicken",
                        strCategoryThumb = "https://www.themealdb.com/images/category/chicken.png",
                        strCategoryDescription = "Chicken is a type of domesticated fowl, a subspecies of the red junglefowl. It is one of the most common and widespread domestic animals, with a total population of more than 19 billion as of 2011.[1] Humans commonly keep chickens as a source of food (consuming both their meat and eggs) and, more rarely, as pets."
                    )
                ),
                error = null
            )
        )
}