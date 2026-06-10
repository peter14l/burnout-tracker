package com.burnouttracker.ui.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burnouttracker.data.remote.MockFirebaseAuth
import com.burnouttracker.domain.model.ExpenseCategory
import com.burnouttracker.domain.usecase.RecordExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExpenseUiState(
    val amount: String = "",
    val selectedCategory: ExpenseCategory = ExpenseCategory.FOOD,
    val description: String = "",
    val isStressRelated: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val recordExpenseUseCase: RecordExpenseUseCase,
    private val mockFirebaseAuth: MockFirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    fun updateAmount(amount: String) {
        // Only allow valid decimal input
        if (amount.isEmpty() || amount.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
            _uiState.value = _uiState.value.copy(amount = amount)
        }
    }

    fun selectCategory(category: ExpenseCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun toggleStressRelated() {
        _uiState.value = _uiState.value.copy(isStressRelated = !_uiState.value.isStressRelated)
    }

    fun saveExpense() {
        val state = _uiState.value
        if (state.isSaving) return

        val amountValue = state.amount.toDoubleOrNull()
        if (amountValue == null || amountValue <= 0) {
            _uiState.value = state.copy(error = "Please enter a valid amount")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true, error = null)

            val userId = mockFirebaseAuth.getCurrentUser()?.uid ?: "mock_user"

            val result = recordExpenseUseCase(
                userId = userId,
                amount = amountValue,
                category = state.selectedCategory,
                description = state.description.ifBlank { null },
                tags = if (state.isStressRelated) listOf("stress") else emptyList()
            )

            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(isSaving = false, isSaved = true)
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = e.message ?: "Failed to save"
                    )
                }
            )
        }
    }
}
