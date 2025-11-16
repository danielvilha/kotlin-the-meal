package com.danielvilha.themeal

import app.cash.turbine.test
import com.danielvilha.themeal.data.dto.MealShortDto
import com.danielvilha.themeal.service.IMealRepository
import com.danielvilha.themeal.ui.category.CategoryUiState
import com.danielvilha.themeal.ui.category.CategoryViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CategoryViewModelTest {

    private val mealRepository: IMealRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when meals are fetched successfully, uiState is updated`() = runTest {
        val category = "Beef"
        val fakeMeals = listOf(MealShortDto(idMeal = "1", strMeal = "Beef Pie", strMealThumb = ""))
        coEvery { mealRepository.getMeals(category) } returns fakeMeals

        val viewModel = CategoryViewModel(mealRepository)

        viewModel.uiState.test {
            // 1. Check the initial state
            assertEquals(CategoryUiState(), awaitItem())

            // 2. Call the function to load data
            viewModel.loadMeals(category)

            // 3. Check the loading state
            assertEquals(CategoryUiState(isLoading = true), awaitItem())

            // 4. Let the coroutine run
            testDispatcher.scheduler.advanceUntilIdle()

            // 5. Check the final success state
            assertEquals(CategoryUiState(isLoading = false, meals = fakeMeals), awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when repository returns error, uiState is updated`() = runTest {
        val category = "Beef"
        val errorMessage = "Network Error"
        coEvery { mealRepository.getMeals(category) } throws Exception(errorMessage)

        val viewModel = CategoryViewModel(mealRepository)

        viewModel.uiState.test {
            // 1. Check the initial state
            assertEquals(CategoryUiState(), awaitItem())

            // 2. Call the function to load data
            viewModel.loadMeals(category)

            // 3. Check the loading state
            assertEquals(CategoryUiState(isLoading = true), awaitItem())

            // 4. Let the coroutine run
            testDispatcher.scheduler.advanceUntilIdle()

            // 5. Check the final error state
            assertEquals(CategoryUiState(isLoading = false, error = errorMessage), awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `whenRetrying_recoversFromError_ifCauseIsFixed`() = runTest {
        val category = "Beef"
        val errorMessage = "No Internet"
        val fakeMeals = listOf(MealShortDto(idMeal = "1", strMeal = "Beef Pie", strMealThumb = ""))

        val viewModel = CategoryViewModel(mealRepository)

        viewModel.uiState.test {
            // Step 1: First attempt (offline) -> Should fail
            coEvery { mealRepository.getMeals(category) } throws Exception(errorMessage)
            viewModel.loadMeals(category)

            assertEquals(CategoryUiState(), awaitItem()) // Initial state
            assertEquals(CategoryUiState(isLoading = true), awaitItem()) // Loading state
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(CategoryUiState(isLoading = false, error = errorMessage), awaitItem()) // Error state

            // Step 2: Second attempt (still offline) -> Should fail again
            viewModel.loadMeals(category)

            assertEquals(CategoryUiState(isLoading = true, error = null), awaitItem()) // Loading state (error is cleared)
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(CategoryUiState(isLoading = false, error = errorMessage), awaitItem()) // Back to error state

            // Step 3: "Internet comes back" - We change the mock's behavior
            coEvery { mealRepository.getMeals(category) } returns fakeMeals

            // Step 4: Final attempt (online) -> Should succeed
            viewModel.loadMeals(category)

            assertEquals(CategoryUiState(isLoading = true, error = null), awaitItem()) // Loading state (error is cleared)
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(CategoryUiState(isLoading = false, meals = fakeMeals), awaitItem()) // Success state

            cancelAndConsumeRemainingEvents()
        }
    }
}