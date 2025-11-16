package com.danielvilha.themeal

import app.cash.turbine.test
import com.danielvilha.themeal.data.dto.CategoryDto
import com.danielvilha.themeal.service.IMealRepository
import com.danielvilha.themeal.ui.categories.CategoryListUiState
import com.danielvilha.themeal.ui.categories.CategoryListViewModel
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
class CategoryListViewModelTest {

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
    fun `when categories are fetched successfully, uiState is updated`() = runTest {
        val fakeCategories = listOf(CategoryDto(idCategory = "1", strCategory = "Beef", strCategoryThumb = "", strCategoryDescription = "Test description"))
        coEvery { mealRepository.getCategories() } returns fakeCategories

        val viewModel = CategoryListViewModel(mealRepository)

        viewModel.uiState.test {
            // 1. Check the initial state
            assertEquals(CategoryListUiState(), awaitItem())

            // 2. Call the function to load data
            viewModel.loadCategories()

            // 3. Check the loading state
            assertEquals(CategoryListUiState(isLoading = true), awaitItem())

            // 4. Let the coroutine run
            testDispatcher.scheduler.advanceUntilIdle()

            // 5. Check the final success state
            assertEquals(CategoryListUiState(isLoading = false, categories = fakeCategories), awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when repository returns error, uiState is updated`() = runTest {
        val errorMessage = "Network Error"
        coEvery { mealRepository.getCategories() } throws Exception(errorMessage)

        val viewModel = CategoryListViewModel(mealRepository)

        viewModel.uiState.test {
            // 1. Check the initial state
            assertEquals(CategoryListUiState(), awaitItem())

            // 2. Call the function to load data
            viewModel.loadCategories()

            // 3. Check the loading state
            assertEquals(CategoryListUiState(isLoading = true), awaitItem())

            // 4. Let the coroutine run
            testDispatcher.scheduler.advanceUntilIdle()

            // 5. Check the final error state
            assertEquals(CategoryListUiState(isLoading = false, error = errorMessage), awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `whenRetrying_recoversFromError_ifCauseIsFixed`() = runTest {
        val errorMessage = "No Internet"
        val fakeCategories = listOf(CategoryDto(idCategory = "1", strCategory = "Beef", strCategoryThumb = "", strCategoryDescription = "Test description"))

        val viewModel = CategoryListViewModel(mealRepository)

        viewModel.uiState.test {
            // Step 1: First attempt (offline) -> Should fail
            coEvery { mealRepository.getCategories() } throws Exception(errorMessage)
            viewModel.loadCategories()

            assertEquals(CategoryListUiState(), awaitItem()) // Initial state
            assertEquals(CategoryListUiState(isLoading = true), awaitItem()) // Loading state
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(CategoryListUiState(isLoading = false, error = errorMessage), awaitItem()) // Error state

            // Step 2: Second attempt (still offline) -> Should fail again
            viewModel.loadCategories()

            assertEquals(CategoryListUiState(isLoading = true, error = null), awaitItem()) // Loading state (error is cleared)
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(CategoryListUiState(isLoading = false, error = errorMessage), awaitItem()) // Back to error state

            // Step 3: "Internet comes back" - We change the mock's behavior
            coEvery { mealRepository.getCategories() } returns fakeCategories

            // Step 4: Final attempt (online) -> Should succeed
            viewModel.loadCategories()

            assertEquals(CategoryListUiState(isLoading = true, error = null), awaitItem()) // Loading state (error is cleared)
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(CategoryListUiState(isLoading = false, categories = fakeCategories), awaitItem()) // Success state

            cancelAndConsumeRemainingEvents()
        }
    }
}