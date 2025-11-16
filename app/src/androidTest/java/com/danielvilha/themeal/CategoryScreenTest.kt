package com.danielvilha.themeal

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.danielvilha.themeal.data.dto.MealShortDto
import com.danielvilha.themeal.ui.category.CategoryScreen
import com.danielvilha.themeal.ui.category.CategoryUiState
import com.danielvilha.themeal.ui.theme.TheMealTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenStateIsLoading_progressBarIsShown() {
        // Arrange & Act
        composeTestRule.setContent {
            TheMealTheme {
                CategoryScreen(
                    category = "Beef",
                    state = CategoryUiState(isLoading = true),
                    onBackClick = {},
                    onMealClick = {},
                    onRetryClick = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("LoadingIndicator").assertIsDisplayed()
    }

    @Test
    fun whenStateIsSuccess_mealListIsShown() {
        val fakeMeals = listOf(
            MealShortDto(idMeal = "1", strMeal = "Beef and Mustard Pie", strMealThumb = ""),
            MealShortDto(idMeal = "2", strMeal = "Beef and Oyster pie", strMealThumb = "")
        )

        composeTestRule.setContent {
            TheMealTheme {
                CategoryScreen(
                    category = "Beef",
                    state = CategoryUiState(isLoading = false, meals = fakeMeals),
                    onBackClick = {},
                    onMealClick = {},
                    onRetryClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Beef").assertIsDisplayed() // TopAppBar title
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
        composeTestRule.onNodeWithText("Beef and Mustard Pie").assertIsDisplayed()
        composeTestRule.onNodeWithText("Beef and Oyster pie").assertIsDisplayed()
    }

    @Test
    fun whenStateIsError_errorMessageIsShown() {
        composeTestRule.setContent {
            TheMealTheme {
                CategoryScreen(
                    category = "Beef",
                    state = CategoryUiState(isLoading = false, error = "Could not load meals"),
                    onBackClick = {},
                    onMealClick = {},
                    onRetryClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Error: Could not load meals").assertIsDisplayed()
    }
}