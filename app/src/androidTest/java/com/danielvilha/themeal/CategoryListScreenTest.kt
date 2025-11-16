package com.danielvilha.themeal

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.danielvilha.themeal.data.dto.CategoryDto
import com.danielvilha.themeal.ui.categories.CategoryListScreen
import com.danielvilha.themeal.ui.categories.CategoryListUiState
import com.danielvilha.themeal.ui.theme.TheMealTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoryListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenStateIsLoading_progressBarIsShown() {
        composeTestRule.setContent {
            TheMealTheme {
                CategoryListScreen(
                    state = CategoryListUiState(isLoading = true),
                    onCategoryClick = {},
                    onRetryClick = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("LoadingIndicator").assertIsDisplayed()
    }

    @Test
    fun whenStateIsSuccess_categoryListIsShown() {
        val fakeCategories = listOf(
            CategoryDto(idCategory = "1", strCategory = "Beef", strCategoryThumb = "", strCategoryDescription = ""),
            CategoryDto(idCategory = "2", strCategory = "Chicken", strCategoryThumb = "", strCategoryDescription = "")
        )

        composeTestRule.setContent {
            TheMealTheme {
                CategoryListScreen(
                    state = CategoryListUiState(isLoading = false, categories = fakeCategories),
                    onCategoryClick = {},
                    onRetryClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Categories").assertIsDisplayed() // TopAppBar title
        composeTestRule.onNodeWithText("Beef").assertIsDisplayed()
        composeTestRule.onNodeWithText("Chicken").assertIsDisplayed()
    }

    @Test
    fun whenStateIsError_errorMessageIsShown() {
        composeTestRule.setContent {
            TheMealTheme {
                CategoryListScreen(
                    state = CategoryListUiState(isLoading = false, error = "Could not load categories"),
                    onCategoryClick = {},
                    onRetryClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Error: Could not load categories").assertIsDisplayed()
    }
}