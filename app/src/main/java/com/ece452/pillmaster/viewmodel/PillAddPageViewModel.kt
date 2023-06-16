package com.ece452.pillmaster.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PillAddPageViewModel @Inject constructor(): ViewModel() {

    // submit a new pill
    fun newPillSubmit(
        pillName: (String),
        direction: (String),
        reminderTime: (String),
        startDate: (String),
        endDate: (String),
        selectedOption: (String),
        isChecked: (Boolean)) {

    }

    // get all Caregivers of current Carereceiver
    // return a List<String>
     fun getCareGivers() {

    }
}