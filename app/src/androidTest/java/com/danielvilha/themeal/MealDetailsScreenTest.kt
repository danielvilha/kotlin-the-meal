package com.danielvilha.themeal

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.danielvilha.themeal.data.dto.MealDetailsDto
import com.danielvilha.themeal.features.mealdetails.MealDetailsScreen
import com.danielvilha.themeal.features.mealdetails.MealDetailsUiState
import com.danielvilha.themeal.ui.theme.TheMealTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MealDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenStateIsLoading_progressBarIsShown() {
        composeTestRule.setContent {
            TheMealTheme {
                MealDetailsScreen(
                    state = MealDetailsUiState(isLoading = true),
                    onBackClick = {},
                    onRetryClick = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("LoadingIndicator").assertIsDisplayed()
    }

    @Test
    fun whenStateIsSuccess_mealDetailsAreShown() {
        val fakeMeal = MealDetailsDto(
            idMeal = "1",
            strMeal = "Spaghetti Carbonara",
            strCategory = "Pasta",
            strArea = "Italian",
            strInstructions = "Cook the pasta...",
            strIngredient1 = "Pasta", strMeasure1 = "200g",
        )

        composeTestRule.setContent {
            TheMealTheme {
                MealDetailsScreen(
                    state = MealDetailsUiState(isLoading = false, selectedMeal = fakeMeal),
                    onBackClick = {},
                    onRetryClick = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("MealTitle").assertIsDisplayed()
        composeTestRule.onNodeWithText("Category: Pasta").assertIsDisplayed()
        composeTestRule.onNodeWithText("• Pasta: 200g").assertIsDisplayed()
        composeTestRule.onNodeWithText("Instructions:").assertIsDisplayed()
    }

    @Test
    fun whenStateIsError_errorMessageIsShown() {
        composeTestRule.setContent {
            TheMealTheme {
                MealDetailsScreen(
                    state = MealDetailsUiState(isLoading = false, error = "Could not load meal"),
                    onBackClick = {},
                    onRetryClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Error: Could not load meal").assertIsDisplayed()
    }
}