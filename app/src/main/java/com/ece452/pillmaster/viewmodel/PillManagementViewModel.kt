package com.ece452.pillmaster.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ece452.pillmaster.model.Pill
import com.ece452.pillmaster.repository.AuthRepository
import com.ece452.pillmaster.repository.PillRepository
import com.ece452.pillmaster.di.FirebaseModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

// View model for Pill management page for viewing and managing pill list
@HiltViewModel
class PillManagementViewModel @Inject constructor(
   private val repository: PillRepository
): ViewModel() {
    // Pill list of the user
    val pills = repository.pills

    val pill = mutableStateOf(Pill())

    // On PillManagementScreen, user can click on each pill to view name, description and info
    // Open pill info viewing screen to show the fields of pill
    fun onGetPill(pillId: String) {
        viewModelScope.launch {
            pill.value = repository.getPill(pillId) ?: Pill()
        }
    }

    // On PillManagementScreen, show each pill's name and a button to delete the pill
    fun onPillDelete(pillId: String) {
        viewModelScope.launch {
            repository.delete(pillId)
        }
    }

}