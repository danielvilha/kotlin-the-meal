package com.danielvilha.themeal.features.mealdetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.danielvilha.themeal.R
import com.danielvilha.themeal.data.dto.MealDetailsDto
import com.danielvilha.themeal.data.dto.getIngredientsList
import com.danielvilha.themeal.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.themeal.ui.preview.LightDarkPreview
import com.danielvilha.themeal.ui.theme.TheMealTheme
import com.danielvilha.themeal.ui.util.ErrorText
import com.danielvilha.themeal.ui.util.ProgressIndicator

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun ScreenPreview(
    @PreviewParameter(MealDetailScreenPreview ::class)
    state: MealDetailsUiState
) {
    TheMealTheme {
        MealDetailsScreen(
            state = state,
            onBackClick = {},
            onRetryClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailsScreen(
    state: MealDetailsUiState,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit
) {
    val meal = state.selectedMeal

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(meal?.strMeal ?: "Details") },
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
                meal == null -> ErrorText(error = "Meal not found", onRetry = onRetryClick)
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    ) {
                        item {
                            if (!meal.strMealThumb.isNullOrBlank()) {
                                AsyncImage(
                                    model = meal.strMealThumb,
                                    contentDescription = meal.strMeal,
                                    placeholder = painterResource(id = R.drawable.ic_meal),
                                    error = painterResource(id = R.drawable.ic_no_meal),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(220.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            Text(
                                text = meal.strMeal ?: "",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.testTag("MealTitle")
                            )

                            meal.strCategory?.let {
                                Text(
                                    text = "Category: $it",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            meal.strArea?.let {
                                Text(
                                    text = "Area: $it",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            val ingredients = meal.getIngredientsList()
                            if (ingredients.isNotEmpty()) {
                                Text(
                                    text = "Ingredients:",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                ingredients.forEach { (ingredient, measure) ->
                                    Text(
                                        modifier = Modifier.padding(start = 16.dp),
                                        text = "• $ingredient: $measure",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            if (!meal.strInstructions.isNullOrBlank()) {
                                Column {
                                    Text(
                                        text = "Instructions:",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = meal.strInstructions,
                                        style = MaterialTheme.typography.bodyMedium
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
class MealDetailScreenPreview : PreviewParameterProvider<MealDetailsUiState> {
    override val values: Sequence<MealDetailsUiState>
        get() = sequenceOf(
            MealDetailsUiState(
                isLoading = false,
                selectedMeal = MealDetailsDto(
                    idMeal = "52874",
                    strMeal = "Beef and Mustard Pie",
                    strMealAlternate = null,
                    strCategory = "Beef",
                    strArea = "British",
                    strInstructions = "Preheat the oven to 150C/300F/Gas 2.\r\nToss the beef and flour together in a bowl with some salt and black pepper.\r\nHeat a large casserole until hot, add half of the rapeseed oil and enough of the beef to just cover the bottom of the casserole.\r\nFry until browned on each side, then remove and set aside. Repeat with the remaining oil and beef.\r\nReturn the beef to the pan, add the wine and cook until the volume of liquid has reduced by half, then add the stock, onion, carrots, thyme and mustard, and season well with salt and pepper.\r\nCover with a lid and place in the oven for two hours.\r\nRemove from the oven, check the seasoning and set aside to cool. Remove the thyme.\r\nWhen the beef is cool and you're ready to assemble the pie, preheat the oven to 200C/400F/Gas 6.\r\nTransfer the beef to a pie dish, brush the rim with the beaten egg yolks and lay the pastry over the top. Brush the top of the pastry with more beaten egg.\r\nTrim the pastry so there is just enough excess to crimp the edges, then place in the oven and bake for 30 minutes, or until the pastry is golden-brown and cooked through.\r\nFor the green beans, bring a saucepan of salted water to the boil, add the beans and cook for 4-5 minutes, or until just tender.\r\nDrain and toss with the butter, then season with black pepper.\r\nTo serve, place a large spoonful of pie onto each plate with some green beans alongside.",
                    strMealThumb = "https://www.themealdb.com/images/media/meals/sytuqu1511553755.jpg",
                    strTags = "Meat,Pie",
                    strYoutube = "https://www.youtube.com/watch?v=nMyBC9staMU",
                    strIngredient1 = "Beef",
                    strIngredient2 = "Plain Flour",
                    strIngredient3 = "Rapeseed Oil",
                    strIngredient4 = "Red Wine",
                    strIngredient5 = "Beef Stock",
                    strIngredient6 = "Onion",
                    strIngredient7 = "Carrots",
                    strIngredient8 = "Thyme",
                    strIngredient9 = "Mustard",
                    strIngredient10 = "Egg Yolks",
                    strIngredient11 = "Puff Pastry",
                    strIngredient12 = "Green Beans",
                    strIngredient13 = "Butter",
                    strIngredient14 = "Salt",
                    strIngredient15 = "Pepper",
                    strMeasure1 = "1kg",
                    strMeasure2 = "2 tbs",
                    strMeasure3 = "2 tbs",
                    strMeasure4 = "200ml",
                    strMeasure5 = "400ml",
                    strMeasure6 = "1 finely sliced",
                    strMeasure7 = "2 chopped",
                    strMeasure8 = "3 sprigs",
                    strMeasure9 = "2 tbs",
                    strMeasure10 = "2 free-range",
                    strMeasure11 = "400g",
                    strMeasure12 = "300g",
                    strMeasure13 = "25g",
                    strMeasure14 = "pinch",
                    strMeasure15 = "pinch",
                    strSource = "https://www.bbc.co.uk/food/recipes/beef_and_mustard_pie_58002",
                    strImageSource = null,
                    strCreativeCommonsConfirmed = null,
                    dateModified = null
                ),
                error = null
            )
        )
}