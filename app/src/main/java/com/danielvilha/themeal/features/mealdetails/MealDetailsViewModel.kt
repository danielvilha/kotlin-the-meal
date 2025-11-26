package com.danielvilha.themeal.features.mealdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielvilha.themeal.service.IMealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealDetailsViewModel @Inject constructor(
    private val repository: IMealRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MealDetailsUiState())
    val uiState: StateFlow<MealDetailsUiState> = _uiState

    fun loadMealDetails(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val meal = repository.getMealDetails(id)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    selectedMeal = meal
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}