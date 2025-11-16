package com.danielvilha.themeal

import app.cash.turbine.test
import com.danielvilha.themeal.data.dto.MealDetailsDto
import com.danielvilha.themeal.service.IMealRepository
import com.danielvilha.themeal.ui.meal.MealDetailsUiState
import com.danielvilha.themeal.ui.meal.MealDetailsViewModel
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
class MealDetailsViewModelTest {

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
    fun `when meal details are fetched successfully, uiState is updated`() = runTest {
        val mealId = "52772"
        val fakeMeal = MealDetailsDto(mealId, "Fake Meal")
        coEvery { mealRepository.getMealDetails(mealId) } returns fakeMeal

        val viewModel = MealDetailsViewModel(mealRepository)

        viewModel.uiState.test {
            // 1. Check the initial state
            assertEquals(MealDetailsUiState(), awaitItem())

            // 2. Call the function to load data
            viewModel.loadMealDetails(mealId)

            // 3. Check the loading state
            assertEquals(MealDetailsUiState(isLoading = true), awaitItem())

            // 4. Let the coroutine run
            testDispatcher.scheduler.advanceUntilIdle()

            // 5. Check the final success state
            assertEquals(MealDetailsUiState(isLoading = false, selectedMeal = fakeMeal), awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when repository returns error, uiState is updated`() = runTest {
        val mealId = "52772"
        val errorMessage = "Network Error"
        coEvery { mealRepository.getMealDetails(mealId) } throws Exception(errorMessage)

        val viewModel = MealDetailsViewModel(mealRepository)

        viewModel.uiState.test {
            // 1. Check the initial state
            assertEquals(MealDetailsUiState(), awaitItem())

            // 2. Call the function to load data
            viewModel.loadMealDetails(mealId)

            // 3. Check the loading state
            assertEquals(MealDetailsUiState(isLoading = true), awaitItem())

            // 4. Let the coroutine run
            testDispatcher.scheduler.advanceUntilIdle()

            // 5. Check the final error state
            assertEquals(MealDetailsUiState(isLoading = false, error = errorMessage), awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `whenRetrying_recoversFromError_ifCauseIsFixed`() = runTest {
        val mealId = "52772"
        val errorMessage = "No Internet"
        val fakeMeal = MealDetailsDto(mealId, "Fake Meal")

        val viewModel = MealDetailsViewModel(mealRepository)

        viewModel.uiState.test {
            // Step 1: First attempt (offline) -> Should fail
            coEvery { mealRepository.getMealDetails(mealId) } throws Exception(errorMessage)
            viewModel.loadMealDetails(mealId)

            assertEquals(MealDetailsUiState(), awaitItem()) // Initial state
            assertEquals(MealDetailsUiState(isLoading = true), awaitItem()) // Loading state
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(MealDetailsUiState(isLoading = false, error = errorMessage), awaitItem()) // Error state

            // Step 2: Second attempt (still offline) -> Should fail again
            viewModel.loadMealDetails(mealId)

            assertEquals(MealDetailsUiState(isLoading = true, error = null), awaitItem()) // Loading state (error is cleared)
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(MealDetailsUiState(isLoading = false, error = errorMessage), awaitItem()) // Back to error state

            // Step 3: "Internet comes back" - We change the mock's behavior
            coEvery { mealRepository.getMealDetails(mealId) } returns fakeMeal

            // Step 4: Final attempt (online) -> Should succeed
            viewModel.loadMealDetails(mealId)

            assertEquals(MealDetailsUiState(isLoading = true, error = null), awaitItem()) // Loading state (error is cleared)
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(MealDetailsUiState(isLoading = false, selectedMeal = fakeMeal), awaitItem()) // Success state

            cancelAndConsumeRemainingEvents()
        }
    }
}