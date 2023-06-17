package com.ece452.pillmaster.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// TODO consider renaming this so as to serve for both uploading and retrieving
// TODO pill info to and from the database.
@HiltViewModel
class PillAddPageViewModel @Inject constructor(): ViewModel() {

    // E.g An item will be
    // ("Aspirin", ("2023-06-16", "2023-06-30"))
    // TODO Use this when back-end is ready.
    val medicineList = mutableStateOf(mutableListOf<Pair<String, Pair<String, String>>>())

    // TODO Static list for testing purpose.
    val testList = mutableStateOf(mutableListOf<Pair<String, Pair<String, String>>>())
    init {
        testList.value = mutableListOf(
            "Aspirin" to ("2023-06-17" to "2023-06-30"),
            "Vitamin D" to ("2023-06-20" to "2023-06-25"),
            "Antibiotics" to ("2023-06-11" to "2023-06-20")
        )
    }



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

    // TODO fetch medicines from database into the medicineList.
    fun fetchMedicineData() {

    }

    // TODO remove a medicine from the database. Mock for now.
    fun removePill(input: String) {
        testList.value.removeIf {
            item ->
            item.first == input
        }
    }

}